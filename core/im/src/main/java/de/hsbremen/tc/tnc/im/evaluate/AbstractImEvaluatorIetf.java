/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.hsbremen.tc.tnc.im.evaluate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImFaultyObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Generic base for an evaluator. Especially important for inheritance.
 *
 *
 */
public class AbstractImEvaluatorIetf implements ImEvaluator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractImEvaluatorIetf.class);

    private final long id;
    private final List<? extends ImEvaluationUnit> evaluationUnits;
    private final ImValueExceptionHandler valueExceptionHandler;

    /**
     * Creates the base for an evaluator using the given
     * evaluation units and the exception handler.
     *
     * @param id the evaluator ID identifying the component instance.
     * @param evaluationUnits the list of evaluations units
     * @param valueExceptionHandler the exception handler
     */
    protected AbstractImEvaluatorIetf(final long id,
            final List<? extends ImEvaluationUnit> evaluationUnits,
            final ImValueExceptionHandler valueExceptionHandler) {
        this.id = id;
        this.evaluationUnits = evaluationUnits;
        this.valueExceptionHandler = valueExceptionHandler;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public List<ImObjectComponent> evaluate(final ImSessionContext context) {

        LOGGER.debug("evaluate() called, with connection state: "
                + context.getConnectionState().toString());

        List<ImObjectComponent> components = new LinkedList<>();
        if (this.evaluationUnits != null) {
            for (ImEvaluationUnit unit : this.evaluationUnits) {
                List<ImAttribute> attributes = unit.evaluate(context);
                if (attributes != null && !attributes.isEmpty()) {
                    components.add(ImComponentFactory.createObjectComponent(
                            (byte) 0, unit.getVendorId(), unit.getType(),
                            // be careful IMC_ID_ANY == IMV_ID_ANY this should
                            // never change in TNC specs
                            this.getId(), TNCConstants.TNC_IMVID_ANY,
                            attributes));
                }
            }
        }
        return components;
    }

    @Override
    public List<ImObjectComponent> handle(
            final List<? extends ImObjectComponent> components,
            final ImSessionContext context) {

        LOGGER.debug("handle() called, with connection state: "
                + context.getConnectionState().toString());

        List<ImObjectComponent> componentList = new LinkedList<>();

        if (this.evaluationUnits == null) {
            return componentList;
        }

        for (ImObjectComponent component : components) {

            boolean useExcl = false;
            if (component.getSourceId()
                        != HSBConstants.HSB_IM_ID_UNKNOWN
                    && component.getDestinationId()
                        != HSBConstants.HSB_IM_ID_UNKNOWN
                    && component.getDestinationId()
                        // be careful IMC_ID_ANY == IMV_ID_ANY this should
                        // never change in TNC specs
                        != TNCConstants.TNC_IMVID_ANY) {
                    
                useExcl = this.checkExclusiveDeliverySupport(context);
            }

            long givenVendorId = component.getVendorId();
            long givenType = component.getType();

            for (ImEvaluationUnit unit : this.evaluationUnits) {
                
                long interestedVendorId = unit.getVendorId();
                long interestedType = unit.getType();
                
                if ((interestedVendorId == TNCConstants.TNC_VENDORID_ANY)
                        || ((givenVendorId == interestedVendorId)
                                && ((interestedType == TNCConstants.TNC_SUBTYPE_ANY)
                                        || (givenType == interestedType)))) {

                    List<ImAttribute> attributes = unit.handle(
                            component.getAttributes(), context);
                    if (component instanceof ImFaultyObjectComponent) {

                        List<ImAttribute> errorAttributes =
                                this.valueExceptionHandler.handle(
                                        (ImFaultyObjectComponent) component);

                        if (errorAttributes != null) {
                            if (attributes != null) {
                                attributes.addAll(errorAttributes);
                            } else {
                                attributes = errorAttributes;
                            }
                        }
                    }
                    if (attributes != null && !attributes.isEmpty()) {
                        componentList.add(ImComponentFactory
                                .createObjectComponent(
                                        ((useExcl) ? PaComponentFlagsEnum.EXCL
                                                .bit() : 0),
                                        unit.getVendorId(),
                                        unit.getType(),
                                        this.getId(),
                                        component.getSourceId(),
                                        attributes));
                    }
                }
            }
        }

        return componentList;
    }

    @Override
    public void terminate() {
        LOGGER.debug("Terminate called. Terminating units...");
        if (this.evaluationUnits != null) {
            for (ImEvaluationUnit unit : this.evaluationUnits) {
                unit.terminate();
            }
        }
    }

    @Override
    public List<ImObjectComponent> lastCall(final ImSessionContext context) {
        LOGGER.debug("lastCall() called, with connection state: "
                + context.getConnectionState().toString());

        List<ImObjectComponent> components = new LinkedList<>();
        if (this.evaluationUnits != null) {
            for (ImEvaluationUnit unit : this.evaluationUnits) {
                List<ImAttribute> attributes = unit.lastCall(context);
                if (attributes != null && !attributes.isEmpty()) {
                    components.add(ImComponentFactory.createObjectComponent(
                            (byte) 0, unit.getVendorId(), unit.getType(),
                            this.getId(), TNCConstants.TNC_IMVID_ANY,
                            attributes));
                }
            }
        }
        return components;
    }

    @Override
    public void notifyConnectionChange(ImSessionContext context) {
        if (this.evaluationUnits != null) {
            for (ImEvaluationUnit unit : this.evaluationUnits) {
                unit.notifyConnectionChange(context);
            }
        }
        
    }
    
    /**
     * Checks if exclusive delivery support to an IM(C/V) is possible.
     * @param context the context, which holds connection specific values
     * @return true if exclusive delivery is supported
     */
    private boolean checkExclusiveDeliverySupport(
            final ImSessionContext context) {
        boolean hasSupport = false;

        try {
            Object o = context
                    .getAttribute(
                            TncCommonAttributeTypeEnum
                            .TNC_ATTRIBUTEID_HAS_EXCLUSIVE);
            if (o instanceof Boolean) {
                hasSupport = (Boolean) o;
            }
        } catch (UnsupportedOperationException | TncException e) {
            LOGGER.info("Could not access attribute from connection. "
                    + e.getMessage());
        }

        return hasSupport;

    }

    /**
     * Returns an unmodifiable list of evaluation units.
     * @return the list of evaluation units.
     */
    protected List<? extends ImEvaluationUnit> getEvaluationUnits() {
        return Collections.unmodifiableList(this.evaluationUnits);
    }
}

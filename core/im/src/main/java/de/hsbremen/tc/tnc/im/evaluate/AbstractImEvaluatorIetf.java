/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
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
            if (component.getCollectorId()
                        != HSBConstants.HSB_IM_ID_UNKNOWN
                    && component.getValidatorId()
                        != HSBConstants.HSB_IM_ID_UNKNOWN
                    && component.getValidatorId()
                        != TNCConstants.TNC_IMVID_ANY) {

                useExcl = this.checkExclusiveDeliverySupport(context);
            }

            for (ImEvaluationUnit unit : this.evaluationUnits) {

                long givenVendorId = component.getVendorId();
                long givenType = component.getType();
                long interestedVendorId = unit.getVendorId();
                long interestedType = unit.getType();

                if ((interestedVendorId == TNCConstants.TNC_VENDORID_ANY)
                        || ((givenVendorId == interestedVendorId)
                                && ((givenType == TNCConstants.TNC_SUBTYPE_ANY)
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
                                        component.getValidatorId(),
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

package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvEvaluationResultEnum;

public class ImvRecommendationObject {

	private final ImvActionRecommendationEnum recommendation;
	
	private final ImvEvaluationResultEnum result;

	public ImvRecommendationObject(){
		this.recommendation = ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION;
		this.result = ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_DONT_KNOW;
	}
	
	public ImvRecommendationObject(ImvActionRecommendationEnum recommendation,
			ImvEvaluationResultEnum result) {
		this.recommendation = recommendation;
		this.result = result;
	}

	/**
	 * @return the recommendation
	 */
	public ImvActionRecommendationEnum getRecommendation() {
		return this.recommendation;
	}

	/**
	 * @return the result
	 */
	public ImvEvaluationResultEnum getResult() {
		return this.result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.recommendation == null) ? 0 : this.recommendation
						.hashCode());
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ImvRecommendationObject other = (ImvRecommendationObject) obj;
		if (this.recommendation != other.recommendation) {
			return false;
		}
		if (this.result != other.result) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ImvRecommendationPair [recommendation=" + this.recommendation.toString()
				+ ", result=" + this.result.toString() + "]";
	}
	
	
	
}

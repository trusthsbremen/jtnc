package de.hsbremen.tc.tnc.report;

import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;


public class ImvRecommendationPair {

	private final ImvActionRecommendationEnum recommendation;
	
	private final ImvEvaluationResultEnum result;

	ImvRecommendationPair(){
		this.recommendation = ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION;
		this.result = ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_DONT_KNOW;
	}
	
	ImvRecommendationPair(ImvActionRecommendationEnum recommendation,
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

	/**
	 * @return the recommendation
	 */
	public long getRecommendationValue() {
		return this.recommendation.number();
	}

	/**
	 * @return the result
	 */
	public long getResultValue() {
		return this.result.code();
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
		ImvRecommendationPair other = (ImvRecommendationPair) obj;
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

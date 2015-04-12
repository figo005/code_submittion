package uk.ac.soton.ecs.experiments.util.dbimporter;

public enum ColumnName {
	START_TIME("starttime"), AVERAGE_RMS("average_rms"), AVERAGE_SENSOR_PAIRS_IN_RANGE(
			"average_sensor_pairs_in_range"), EXPANDED_PARTIAL_NODES(
			"expanded_partial_nodes"), TOTAL_PARTIAL_NODES(
			"total_partial_nodes"), CACHE_MISSES("cache_misses"), FUNCTION_CALLS(
			"function_calls"), BOUNDED_MS_TREE_VALUE("bounded_ms_tree_value"), BOUNDED_MS_FACTOR_GRAPH_VALUE(
			"bounded_ms_factor_graph_value"), BOUNDED_MS_OPTIMAL_VALUE(
			"bounded_ms_optimal_value"), BOUNDED_MS_UPPER_BOUND(
			"bounded_ms_upper_bound"), MESSAGE_SIZE("message_size"), BOUNDED_MS_APPROX_RATIO(
			"bounded_ms_approx_ratio"), COMMUNICATION_PROB("communication_prob"), COMMUNICATION_RANGE(
			"communication_range"), END_TIME("endtime"), LENGTHSCALE(
			"lengthscale"), MESSAGE_COUNT("message_count"), SENSOR_COUNT(
			"sensor_count"), TIME_SCALE("timescale"), ID("id"), CAPTURE_TIME(
			"capturetime"), PATROLLING_LOSS("patrollingloss"), AVERAGE_FIELD_VALUE(
			"average_field_value"), AVERAGE_ENTROPY("average_entropy"), RANDOM_SEED(
			"seed"), GAMMA("gamma"), STATE_COUNT_BEFORE("state_count_before"), STATE_COUNT_AFTER(
			"state_count_after"), TAU("tau"), MAX_FIELD_VALUE("max_field_value"), OBSERVATION_VALUE(
			"actual_observation_value"), UNCERTAINTY_INCREMENT(
			"uncertainty_increment"), EXPECTED_REWARD("expected_reward"), ACTUAL_REWARD(
			"actual_reward");

	private String name;

	private ColumnName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}

package com.deveuge.quizial.util;

public class Constants {

	public static final int MAX_ACCOUNTS_BY_IP = 2;
	public static final int VERIFICATION_TOKEN_EXPIRE_MINUTES = 60*24;
	public static final String DEFAULT_IMAGE = "default.jpg";
	public static final String DEFAULT_QUESTION_IMAGE = "https://media.giphy.com/media/l4FGroaKiE5uuMBiM/giphy.gif";
	public static final String DEFAULT_RESULT_IMAGE = "https://media.giphy.com/media/5UCpmbzvZKQCfuF2P2/giphy.gif";
	
	public static final String ALERT_CLASS = "alertClass";
	public static final String ALERT_MESSAGE = "alertMessage";
	
	public static final String RESULTS_DASHBOARD = "2";	// TODO: Set to 9
	public static final int RESULTS_PER_PAGE = 2;		// TODO: Set to 5
	public static final int RESULTS_FIRST_PAGE = 0;

	private Constants() {
		super();
	}
}

package ua.gexlq.TelegramStudyBot.service;

public class ConditionChecker {

	// FOR CHANGING OR SETTING USER DATA
	public static boolean isChangeLanguage(String callBackData) {

		return callBackData.contains("changeUserLanguageTo");
	}

	public static boolean isCallBackDataIsFaculty(String callBackData) {
		return callBackData.contains("setUserFacultyTo");
	}

	public static boolean isCallBackDataIsSpecialization(String callBackData) {

		return callBackData.contains("setUserSpecializationTo");
	}

	public static boolean isCallBackDataIsSemester(String callBackData) {

		return callBackData.contains("setUserSemesterTo");
	}

	// FOR PICKING WORK
	public static boolean isCallBackDataIsSubject(String callBackData) {
		return callBackData.contains("selectSubject");
	}

	public static boolean isCallBackDataIsWorkType(String callBackData) {
		return callBackData.contains("selectWorkType");
	}

	public static boolean isCallBackDataIsWork(String callBackData) {
		return callBackData.contains("selectWork");
	}

	//
	public static boolean isCallBackDataIsDownload(String callBackData) {
		return callBackData.contains("download");
	}

	public static boolean isCallBackDataIsUpload(String callBackData) {
		return callBackData.contains("upload");
	}

	//	

	public static boolean isCallBackDataIsBack(String callBackData) {

		return callBackData.contains("back");
	}

	public static boolean isCallBackDataIsNext(String callBackData) {
		return callBackData.contains("next");
	}

}

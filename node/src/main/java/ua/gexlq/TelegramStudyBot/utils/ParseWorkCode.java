package ua.gexlq.TelegramStudyBot.utils;

import org.springframework.stereotype.Component;

@Component
public class ParseWorkCode {

	private final int facultyId = 0;

	private final int specializationId = 1;

	private final int semesterId = 2;

	private final int subjectId = 3;

	private final int workTypeId = 4;

	private final int workId = 5;

	private final int workVariantId = 6;

	public String getFacultyByWorkCode(String workCode) {
		String[] numbers = workCode.split("\\.");

		return numbers[facultyId];
	}

	public String getSpecializationByWorkCode(String workCode) {
		String[] numbers = workCode.split("\\.");

		return numbers[specializationId];
	}

	public String getSemesterByWorkCode(String workCode) {
		String[] numbers = workCode.split("\\.");

		return numbers[semesterId];
	}

	public String getSubjectByWorkCode(String workCode) {
		String[] numbers = workCode.split("\\.");

		return numbers[subjectId];
	}

	public String getWorkTypeByWorkCode(String workCode) {
		String[] numbers = workCode.split("\\.");

		return numbers[workTypeId];
	}

	public String getWorkByWorkCode(String workCode) {
		String[] numbers = workCode.split("\\.");

		if (numbers[workTypeId].equals("1") || numbers[workTypeId].equals("2"))
			return numbers[workId - 2];
		return numbers[workId];
	}

	public String getWorkVariantByWorkCode(String workCode) {
		String[] numbers = workCode.split("\\.");

		if (numbers[workTypeId].equals("1") || numbers[workTypeId].equals("2"))
			return numbers[workVariantId - 1];

		return numbers[workVariantId];
	}

}

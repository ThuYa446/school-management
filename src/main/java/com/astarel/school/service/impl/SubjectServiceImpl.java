package com.astarel.school.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.model.dto.SubjectDto;
import com.astarel.school.model.entity.Student;
import com.astarel.school.model.entity.Subject;
import com.astarel.school.repository.SubjectRepository;
import com.astarel.school.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService{
	
	@Autowired
	SubjectRepository subjectRepository;
	
	ModelMapper modelMapper = new ModelMapper();

	private List<SubjectDto> subjectListToSubjectDto(List<Subject> subjects) {
		if(subjects !=null) {
			List<SubjectDto> subjectDtos = new ArrayList<SubjectDto>();
			
			for(Subject subject: subjects){
				subjectDtos.add(this.subjectToSubjectDto(subject));
			}
			return subjectDtos;
		}
		return new ArrayList<SubjectDto>();
	}
	
	private SubjectDto subjectToSubjectDto(Subject subject) {
		SubjectDto subjectDto = modelMapper.map(subject, SubjectDto.class);
		if(subject.getStudent() != null) {
			List<StudentDto> students = this.studentListToStudentDto(subject.getStudent());
			subjectDto.setStudentDto(students);
		}
		return subjectDto;
	}
	
	private List<StudentDto> studentListToStudentDto(List<Student> students) {
		if(students != null) {
			List<StudentDto> studentDto = new ArrayList<StudentDto>();
			
			for(Student student: students){
				StudentDto stuDto = modelMapper.map(student, StudentDto.class);
				studentDto.add(stuDto);
			}
			return studentDto;
		}
		return new ArrayList<StudentDto>();
	}
	
	private List<Student> studentDtoToStudentList(List<StudentDto> studentsDto) {
		if(studentsDto != null) {
			List<Student> students = new ArrayList<Student>();
			for(StudentDto studentDto: studentsDto){
				Student student = modelMapper.map(studentDto, Student.class);
				students.add(student);
			}
			return students;
		}
		return new ArrayList<Student>();
	}
	
	@Override
	public List<SubjectDto> getAllSubjects() {
		// TODO Auto-generated method stub
		List<Subject> subjects = this.subjectRepository.findAll();
		return this.subjectListToSubjectDto(subjects);
	}
	
	@Override
	public SubjectDto getSubjectById(Long id) {
		// TODO Auto-generated method stub
		Subject subject = this.subjectRepository.findSubjectById(id).get();
		return this.subjectToSubjectDto(subject);
	}

	@Override
	public Boolean isExistSubject(String title) {
		// TODO Auto-generated method stub
		return this.subjectRepository.findSubjectByTitle(title).isPresent();
	}

	@Override
	public Boolean findSubjectById(Long Id) {
		// TODO Auto-generated method stub
		return this.subjectRepository.findSubjectById(Id).isPresent();
	}

	@Override
	public SubjectDto saveSubject(SubjectDto subjectDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		List<Student> students = this.studentDtoToStudentList(subjectDto.getStudentDto());
		subjectDto.setStudentDto(null);
		Subject subject = modelMapper.map(subjectDto, Subject.class);
		if(students != null) {
			for (Student student : students) {
				student.setSubject(subject);
			}
			subject.setStudent(students);
		}
		if (!this.isExistSubject(subject.getTitle())) {
			subject = this.subjectRepository.save(subject);
		} else {
			throw new ApiErrorResponse("1001", "Subject already exist");
		}
		SubjectDto subjDto = modelMapper.map(subject, SubjectDto.class);
		subjDto.setStudentDto(this.studentListToStudentDto(students));
		return subjDto;
	}

	@Override
	public SubjectDto updateSubject(SubjectDto subjectDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		List<Student> students = this.studentDtoToStudentList(subjectDto.getStudentDto());
		subjectDto.setStudentDto(null);
		Subject subject = modelMapper.map(subjectDto, Subject.class);
		if(students != null) {
			for (Student student : students) {
				student.setSubject(subject);
			}
			subject.setStudent(students);
		}
		if (this.findSubjectById(subjectDto.getId())) {
			if(this.subjectRepository.findSubjectByTitle(subjectDto.getTitle()).isEmpty()) {
				subject = this.subjectRepository.save(subject);
			}else {
				Subject subjct = this.subjectRepository.findSubjectByTitle(subjectDto.getTitle()).get();
				if(subjct.isEqualId(subject.getId())) {
					subject = this.subjectRepository.save(subject);
				}else {
					throw new ApiErrorResponse("1003", "Subject title must be unique.");
				}
			}
		} else {
			throw new ApiErrorResponse("1002", "No such subject with id - "+subjectDto.getId()+" found");
		}
		SubjectDto subjtDto = modelMapper.map(subject, SubjectDto.class);
		subjtDto.setStudentDto(this.studentListToStudentDto(students));
		return subjtDto;
	}

	@Override
	public void deleteSubjectById(Long id) {
		// TODO Auto-generated method stub
		this.subjectRepository.deleteById(id);
	}
}

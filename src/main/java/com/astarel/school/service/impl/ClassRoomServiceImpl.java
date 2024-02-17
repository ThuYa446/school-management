package com.astarel.school.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.ClassRoomDto;
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.model.entity.ClassRoom;
import com.astarel.school.model.entity.Student;
import com.astarel.school.repository.ClassRoomRepository;
import com.astarel.school.repository.SubjectRepository;
import com.astarel.school.service.ClassRoomService;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {

	@Autowired
	ClassRoomRepository classRoomRepository;
	
	@Autowired
	SubjectRepository subjectRepository;

	ModelMapper modelMapper = new ModelMapper();
	
	private List<ClassRoomDto> classRoomListToClassRoomDto(List<ClassRoom> classRoooms) {
		List<ClassRoomDto> classRoomDto = new ArrayList<ClassRoomDto>();
		
		for(ClassRoom room: classRoooms){
			classRoomDto.add(this.classRoomToClassRoomDto(room));
		}
		return classRoomDto;
	}
	
	private ClassRoomDto classRoomToClassRoomDto(ClassRoom classRoom) {
		ClassRoomDto roomDto = modelMapper.map(classRoom, ClassRoomDto.class);
		if(classRoom.getStudent() != null) {
			List<StudentDto> students = this.studentListToStudentDto(classRoom.getStudent());
			roomDto.setStudentDto(students);
		}
		return roomDto;
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
	public Boolean isExistClassRoom(String className) {
		// TODO Auto-generated method stub
		return this.classRoomRepository.findClassRoomByClassName(className).isPresent();
	}
	
	@Override
	public Boolean findClassRoomById(Long Id) {
		// TODO Auto-generated method stub
		return this.classRoomRepository.findClassRoomById(Id).isPresent();
	}
	
	@Override
	public ClassRoomDto getClassRoomById(Long id) {
		// TODO Auto-generated method stub
		ClassRoom classRoom = this.classRoomRepository.findClassRoomById(id).get();
		return this.classRoomToClassRoomDto(classRoom);
	}

	@Override
	public ClassRoomDto saveClassRoom(ClassRoomDto classRoomDto) throws ApiErrorResponse {
		// TODO Auto-generated method stub
		List<Student> students = this.studentDtoToStudentList(classRoomDto.getStudentDto());
		classRoomDto.setStudentDto(null);
		ClassRoom classRoom = modelMapper.map(classRoomDto, ClassRoom.class);
		if(students != null) {
			for (Student student : students) {
				student.setClassRoom(classRoom);
				if(this.subjectRepository.getSubjectByStudentId(students.get(0).getId()).isPresent()) {
					student.setSubject(this.subjectRepository.getSubjectByStudentId(students.get(0).getId()).get());
				}
			}
			classRoom.setStudent(students);
		}
		if (!this.isExistClassRoom(classRoom.getClassName())) {
			if(classRoom.getStudent().size() > 500) {
				throw new ApiErrorResponse("1005", "Maximum allowed student for each class is 500.");
			}
			classRoom = this.classRoomRepository.save(classRoom);
		} else {
			throw new ApiErrorResponse("1001", "ClassRoom already exist.");
		}
		ClassRoomDto classDto = modelMapper.map(classRoom, ClassRoomDto.class);
		classDto.setStudentDto(this.studentListToStudentDto(students));
		return classDto;
	}

	@Override
	public ClassRoomDto updateClassRoom(ClassRoomDto classRoomDto) throws ApiErrorResponse{
		// TODO Auto-generated method stub
		List<Student> students = this.studentDtoToStudentList(classRoomDto.getStudentDto());
		classRoomDto.setStudentDto(null);
		ClassRoom classRoom = modelMapper.map(classRoomDto, ClassRoom.class);
		if(students != null) {
			for (Student student : students) {
				student.setClassRoom(classRoom);
				if(this.subjectRepository.getSubjectByStudentId(students.get(0).getId()).isPresent()) {
					student.setSubject(this.subjectRepository.getSubjectByStudentId(students.get(0).getId()).get());
				}
			}
			classRoom.setStudent(students);
		}
		if (this.findClassRoomById(classRoomDto.getId())) {
			int totalAttendedStudent = this.classRoomRepository.getTotalNumberOfStudentByClassId(classRoom.getId());
			if((totalAttendedStudent+classRoom.getStudent().size()) > 500) {
				throw new ApiErrorResponse("1005", "Maximum allowed student for each class is 500.\n"
						+ "There are total number of students who already attend the class are "+ totalAttendedStudent);
			}
			if(this.classRoomRepository.findClassRoomByClassName(classRoomDto.getClassName()).isEmpty()) {
				classRoom = this.classRoomRepository.save(classRoom);
			}else {
				ClassRoom room = this.classRoomRepository.findClassRoomByClassName(classRoomDto.getClassName()).get();
				if(room.isEqualId(classRoom.getId())) {
					classRoom = this.classRoomRepository.save(classRoom);
				}else {
					throw new ApiErrorResponse("1003", "ClassRoom name must be unique.");
				}
			}
		} else {
			throw new ApiErrorResponse("1002", "No such class room with id - "+classRoomDto.getId()+" found");
		}
		ClassRoomDto classDto = modelMapper.map(classRoom, ClassRoomDto.class);
		classDto.setStudentDto(this.studentListToStudentDto(students));
		return classDto;
	}

	@Override
	public void deleteClassRoomById(Long id) {
		// TODO Auto-generated method stub
		this.classRoomRepository.deleteById(id);
	}

	@Override
	public List<ClassRoomDto> getAllClassRoom() {
		// TODO Auto-generated method stub
		List<ClassRoom> classRooms = this.classRoomRepository.findAll();
		return this.classRoomListToClassRoomDto(classRooms);
	}

}

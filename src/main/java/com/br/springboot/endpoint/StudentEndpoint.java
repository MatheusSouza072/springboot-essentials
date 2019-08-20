package com.br.springboot.endpoint;


import com.br.springboot.error.CustomErrorType;
import com.br.springboot.error.ResourceNotFoundException;
import com.br.springboot.model.Student;
import com.br.springboot.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.Valid;


@RestController
@RequestMapping("students")
public class StudentEndpoint {
    private final StudentRepository studentDAO;

    @Autowired
    public StudentEndpoint(StudentRepository studentDAO) {

        this.studentDAO = studentDAO;
    }

    @GetMapping
    public ResponseEntity<?> listAll(Pageable pageable) {
        return new ResponseEntity<>(studentDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentByID(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails);
        verifyIfStudentExists(id);
        Student student = studentDAO.findById(id).get();

        if (student == null)
            throw new ResourceNotFoundException("Student not found for id: " + id);
        return new ResponseEntity<>(student, HttpStatus.OK);


    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name) {
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor =  Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
//        studentDAO.save(student);
//        student.setId(null);
//        studentDAO.save(student);
//        if(true)
//            throw  new RuntimeException("Test Transaction");
//        studentDAO.save(student);
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Student student) {
        verifyIfStudentExists(student.getId());
        studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void verifyIfStudentExists(Long id) {
        if (studentDAO.findById(id).get() == null)
            throw new ResourceNotFoundException("Student not found for id: " + id);

    }
}

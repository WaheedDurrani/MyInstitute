package com.smartSchool.controller;

import com.smartSchool.exceptions.CustomException;
import com.smartSchool.models.AcademicSessionYear;
import com.smartSchool.models.AcademicSessionYear;
import com.smartSchool.repository.AcademicSessionYearRepository;

import com.smartSchool.utils.AppUtility;
import com.smartSchool.utils.DataBaseConstrainst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/academicSessionController")
public class AcademicYearController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AcademicSessionYearRepository academicSessionYearRepository;

    /*************************************************************************************
     *                       CRUD FOR ACADEMICSESSIONS                                   *
     *                         13 OCT 2022                                               *
     * ********************************************************************************** /


     /**
     * @param obj
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<AcademicSessionYear> createAcademicSessionYear(@RequestBody AcademicSessionYear obj) throws CustomException {
        ResponseEntity<AcademicSessionYear> result;
        if(!AppUtility.isEmptyOrNull(obj.getId())){
            throw new CustomException("ID MUST BE NULL");
        }
        try {
            obj.setCreatedDate(AppUtility.getCurrentTimeStamp());
            obj.setModifiedDate(AppUtility.getCurrentTimeStamp());
            AcademicSessionYear AcademicSessionYearObj = academicSessionYearRepository.save(obj);
            result = new ResponseEntity<>(AcademicSessionYearObj, HttpStatus.CREATED);
        }
        catch (DataIntegrityViolationException e) {
            logger.error( "----- Academic Session Post -> " + DataBaseConstrainst.UNIQ);
            throw new RuntimeException(DataBaseConstrainst.UNIQ);
        }
        catch (Exception e){
            logger.error( "-----  Academic Session Post SERVER ERROR ---- ");
            result = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/")
    public ResponseEntity<AcademicSessionYear> updateAcademicSessionYear(@RequestBody AcademicSessionYear obj)
            throws CustomException {
        if (AppUtility.isEmptyOrNull(obj.getId())) {
            throw new CustomException("Id Can not be null");
        } else if (AppUtility.isEmptyOrNull(obj.getName()) || AppUtility.isEmptyOrNull(obj.getInstituteId())) {
            throw  new CustomException("Mandatory Fields are Empty");
        } else {
            try {
                obj.setModifiedDate(AppUtility.getCurrentTimeStamp());
                obj = academicSessionYearRepository.save(obj);
            }
            catch (DataIntegrityViolationException e) {
                logger.error( "----- Academic Session put -> " +DataBaseConstrainst.UNIQ);
                throw new RuntimeException(DataBaseConstrainst.UNIQ);
            }
            catch (Exception e) {
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return ResponseEntity.ok(obj);
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<AcademicSessionYear> getAcademicSessionYearById(@PathVariable("id") Long id) {
        ResponseEntity<AcademicSessionYear> result;
        Optional<AcademicSessionYear> obj = Optional.ofNullable(academicSessionYearRepository.getByIdEntityGeneric(id,false));

        if (obj.isPresent()) {
            result = new ResponseEntity<>(obj.get(), HttpStatus.OK);
        } else {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAcademicSessionYear(@PathVariable("id") Long id) {
        ResponseEntity<HttpStatus> result;
        try {
            academicSessionYearRepository.markAsDeletedById(id,true,AppUtility.getDeleteStamp());
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * @return
     */
    @DeleteMapping("/AcademicSessionYears")
    public ResponseEntity<HttpStatus> deleteAllAcademicSessionYears() {
        ResponseEntity<HttpStatus> result;
        try {
            academicSessionYearRepository.deleteAll();
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }
}

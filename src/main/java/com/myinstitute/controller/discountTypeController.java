package com.myinstitute.controller;


import com.myinstitute.models.DiscountType;
import com.myinstitute.repository.DiscountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/discountTypesController")
public class discountTypeController {

    @Autowired
    DiscountTypeRepository discountTypeRepo;


    /**
     * @param discountType
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<DiscountType> createDiscountType(@RequestBody DiscountType discountType) {
        ResponseEntity<DiscountType> result;
        try {
            DiscountType discountTypeObj = discountTypeRepo.save(discountType);
            result = new ResponseEntity<>(discountTypeObj, HttpStatus.CREATED);
        } catch (Exception e) {
            result = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/institute/{id}")
    public ResponseEntity<DiscountType> getDiscountTypeById(@PathVariable("id") Long id) {
        ResponseEntity<DiscountType> result;
        Optional<DiscountType> tutorialData = discountTypeRepo.findById(id);

        if (tutorialData.isPresent()) {
            result = new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
        } else {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    /**
     * @param instituteId
     * @return
     */
    @GetMapping("/instituteId/{instituteId}")
    public ResponseEntity<List<DiscountType>> findAllByInstituteId(@PathVariable("instituteId") Long instituteId) {
        ResponseEntity<List<DiscountType>> result;
        try {
            List<DiscountType> tutorials = discountTypeRepo.getAll(instituteId, false);

            if (tutorials.isEmpty()) {
                result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                result = new ResponseEntity<>(tutorials, HttpStatus.OK);
            }
        } catch (Exception e) {
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<HttpStatus> deleteDiscountType(@PathVariable("id") Long id) {
        ResponseEntity<HttpStatus> result;
        try {
            discountTypeRepo.deleteById(id);
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * @return
     */
    @DeleteMapping("/discountTypes")
    public ResponseEntity<HttpStatus> deleteAllDiscountTypes() {
        ResponseEntity<HttpStatus> result;
        try {
            discountTypeRepo.deleteAll();
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

}
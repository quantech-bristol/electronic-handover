package com.quantech.service.WardService;

import com.quantech.model.Ward;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public interface WardService
{

    /**
     * Finds all wards currently stored in the repository.
     * @return A list of all wards currently stored, potentially corresponding to all wards in a hospital.
     */
    List<Ward> getAllWards() ;

    /**
     * Finds a ward corresponding to the unique id, stored in the repository.
     * @param id The id for which a ward is a associated with.
     * @return A ward corresponding to the id if one exists, null otherwise.
     */
    Ward getWard(Long id);

    /**
     * Save a given ward into the repository.
     * @param ward The ward to be saved.
     */
    void saveWard(Ward ward);

    /**
     * Deletes a given ward from the repository.
     * @param id The id corresponding to the ward that is to be deleted.
     */
    void deleteWard(Long id);

    void CheckValidity(BindingResult result, Ward ward);

}

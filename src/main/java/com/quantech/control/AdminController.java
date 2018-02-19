package com.quantech.control;

import com.quantech.config.SecurityRoles;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.Category;
import com.quantech.model.Doctor;
import com.quantech.model.Risk;
import com.quantech.model.Ward;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.model.user.UserInfo;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorServiceImpl;
import com.quantech.service.RiskService.RiskServiceImpl;
import com.quantech.service.UserService.UserServiceImpl;
import com.quantech.service.WardService.WardServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DoctorServiceImpl doctorService;

    @Autowired
    WardServiceImpl wardService;

    @Autowired
    RiskServiceImpl riskService;

    @Autowired
    CategoryServiceImpl categoryService;

    @GetMapping(value="/admin")
    public String adminPage() {
        return "admin/admin";
    }

    @GetMapping(value="/admin/createUser")
    public String createUser(Model model) {
        model.addAttribute("UserFiltering",false);
        model.addAttribute("Title","Create User");
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("postUrl", "/admin/createUser");
        return "admin/createUser";
    }

    @PostMapping(value="/admin/createUser")
    public String createUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Errors errors) {
        userService.CheckValidity(result, true, user);
        if (errors.hasErrors()) {
            return "admin/createUser";
        } else {
            UserCore newUser = user.ToUserCore();
            userService.saveUser(newUser);
            if (newUser.hasAuth(SecurityRoles.Doctor)) {
                Doctor newDoc = new Doctor(newUser);
                doctorService.saveDoctor(newDoc);
            }
            return "redirect:/admin";
        }
    }

    @GetMapping(value="/admin/editUsers")
    public String editUsers() {
        return "admin/editUsers";
    }

    @GetMapping(value="/admin/editUserFilter")
    public String editUsersFilter(Model model)
    {
        model.addAttribute("UserFiltering",true);
        model.addAttribute("Title","Select User Details");
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("postUrl", "/admin/editUserFilter");
        return "admin/userFilterFields";
    }

    @PostMapping(value="/admin/editUserFilter")
    public String editUsersFilterPost(@ModelAttribute("usercore") UserFormBackingObject user, RedirectAttributes redirectAttrs)
    {
        List<UserCore> matchingUsers = userService.findMatchesFromFilter(user);
        redirectAttrs.addFlashAttribute("MatchedUsers",matchingUsers);
        return "redirect:editUserSelect";
    }

    @GetMapping(value = "/admin/editUserSelect")
    public String editUsersSelect(@ModelAttribute("MatchedUsers") List<UserCore> users, Model model)
    {

        return "admin/editUserSelect";
    }

    @GetMapping(value = "/admin/editUser/{id}")
    public String editUser(Model model, @PathVariable("id") long id )
    {
        UserCore user = userService.findUserById(id);

        model.addAttribute("UserFiltering","True");
        model.addAttribute("Title","Edit User");
        model.addAttribute("usercore", new UserFormBackingObject(user));
        model.addAttribute("postUrl", "/admin/editUser/" + id);
        return "admin/createUser";
    }

    @PostMapping(value = "/admin/editUser/{id}")
    public String editUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Errors errors, @PathVariable("id") long id)
        {
            userService.CheckValidity(result, false, user);
            if (errors.hasErrors()) {
                return "admin/createUser";
            }
            else {
                UserCore userToEdit = userService.findUserById(user.getId());
                userToEdit.updateValues(user);
                userService.saveUser(userToEdit);
                return "redirect:/admin";
            }
        }
    @DeleteMapping(value = "/admin/deleteUser/{id}")
    public String DeleteUser(@PathVariable("id") long id)
    {
        UserCore user = userService.findUserById(id);
        doctorService.deleteDoctor(user);
        userService.deleteUserById(id);

        return "redirect:/admin";
    }


    @GetMapping(value="/admin/manageWards")
    public String manageWards(Model model) {
        model.addAttribute("newWard", new Ward());
        model.addAttribute("wards", wardService.getAllWards());
        return "admin/manageWards";
    }

    @PostMapping(value="/admin/manageWards")
    public String addWard(@ModelAttribute Ward ward) {
        wardService.saveWard(ward);
        return "redirect:/admin/manageWards";
    }

    @PostMapping(value="/admin/renameWard")
    public String renameWard(@ModelAttribute Ward ward, @RequestParam(value = "id", required=true) Long id) {
        ward.setId(id);
        wardService.saveWard(ward);
        return "redirect:/admin/manageWards";
    }

    @GetMapping(value="/admin/deleteWard")
    public String deleteWard(@ModelAttribute Ward ward, @RequestParam(value = "id", required=true) Long id) {
        wardService.deleteWard(id);
        return "redirect:/admin/manageWards";
    }

    @GetMapping(value="/admin/manageCategories")
    public String manageCategories(Model model) {
        model.addAttribute("newCat", new Category());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "admin/manageCategories";
    }
    @RequestMapping(value="/admin/addCategory", method=RequestMethod.POST)
    public String addCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/manageCategories";
    }

    @GetMapping(value="/admin/manageRisks")
    public String manageRisks(Model model) {
        model.addAttribute("newRisk", new Risk());
        model.addAttribute("risks", riskService.getAllRisks());
        return "admin/manageRisks";
    }

    @PostMapping(value="/admin/manageRisks")
    public String addRisk(@ModelAttribute Risk risk, Model model) {
        riskService.saveRisk(risk);
        model.addAttribute("newRisk", new Risk());
        model.addAttribute("risks", riskService.getAllRisks());
        return "admin/manageRisks";
    }

}
package com.quantech.control;

import com.quantech.config.SecurityRoles;
import com.quantech.misc.AuthFacade.IAuthenticationFacade;
import com.quantech.model.Category;
import com.quantech.model.Doctor;
import com.quantech.model.Log.Log;
import com.quantech.model.Log.LogOperations.AddUser;
import com.quantech.model.Log.LogOperations.LogFilterBackingObject;
import com.quantech.model.Risk;
import com.quantech.model.Ward;
import com.quantech.model.user.UserCore;
import com.quantech.model.user.UserFormBackingObject;
import com.quantech.model.user.UserInfo;
import com.quantech.service.CategoryService.CategoryServiceImpl;
import com.quantech.service.DoctorService.DoctorService;
import com.quantech.service.LoggingService.LogServiceImpl;
import com.quantech.service.RiskService.RiskServiceImpl;
import com.quantech.service.UserService.UserService;
import com.quantech.service.UserService.UserServiceImpl;
import com.quantech.service.WardService.WardServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Controller
@SessionAttributes({"logList","MatchedUsersMap"})
public class AdminController {

    @Autowired
    LogServiceImpl logService;

    @Autowired
    IAuthenticationFacade authenticator;

    @Autowired
    UserService userService;

    @Autowired
    DoctorService doctorService;

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
                userService.createUser(user);
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
    public String editUsersFilterPost(@ModelAttribute("usercore") UserFormBackingObject user, Model model)
    {
        HashMap<Long, UserCore> userCoreHashMap = userService.findMatchesFromFilter(user);

        model.addAttribute("MatchedUsersMap",userCoreHashMap);
        return "redirect:editUserSelect";
    }

    @GetMapping(value = "/admin/editUserSelect")
    public String editUsersSelect(@SessionAttribute("MatchedUsersMap")  HashMap<Long, UserCore> users, Model model)
    {
        model.addAttribute("MatchedUsers",users.values());
        return "admin/editUserSelect";
    }

    @GetMapping(value = "/admin/editUser/{id}")
    public String editUser(Model model, @PathVariable("id") long id )
    {
        UserCore user = userService.findUserById(id);

        model.addAttribute("UserFiltering","True");
        model.addAttribute("Title","Edit User");
        model.addAttribute("usercore", new UserFormBackingObject(user));
        model.addAttribute("postUrl", "/admin/editUser");
        return "admin/createUser";
    }

    @PostMapping(value = "/admin/editUser")
    public String editUser(@Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Errors errors)
        {
            userService.CheckValidity(result, false, user);
            if (errors.hasErrors()) {
                return "admin/createUser";
            }
            else {
               userService.editUser(user);
                return "redirect:/admin";
            }
        }
    @DeleteMapping(value = "/admin/deleteUser/{id}")
    public String DeleteUser(@PathVariable("id") long id)
    {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    // Wards

    @GetMapping(value="/admin/manageWards")
    public String manageWards(Model model) {
        model.addAttribute("newWard", new Ward());
        model.addAttribute("wards", wardService.getAllWards());
        return "admin/manageWards";
    }

    @PostMapping(value="/admin/addWard")
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

    // Categories

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

    @PostMapping(value="/admin/renameCategory")
    public String renameCategory(@ModelAttribute Category category, @RequestParam(value = "id", required=true) Long id) {
        category.setId(id);
        categoryService.saveCategory(category);
        return "redirect:/admin/manageCategories";
    }

    @GetMapping(value="/admin/deleteCategory")
    public String deleteRisk(@ModelAttribute Category category, @RequestParam(value = "id", required=true) Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/manageCategories";
    }

    // Risks

    @GetMapping(value="/admin/manageRisks")
    public String manageRisks(Model model) {
        model.addAttribute("newRisk", new Risk());
        model.addAttribute("risks", riskService.getAllRisks());
        return "admin/manageRisks";
    }

    @PostMapping(value="/admin/addRisk")
    public String addRisk(@ModelAttribute Risk risk) {
        riskService.saveRisk(risk);
        return "redirect:/admin/manageRisks";
    }

    @PostMapping(value="/admin/renameRisk")
    public String renameRisk(@ModelAttribute Risk risk, @RequestParam(value = "id", required=true) Long id) {
        risk.setId(id);
        riskService.saveRisk(risk);
        return "redirect:/admin/manageRisks";
    }

    @GetMapping(value="/admin/deleteRisk")
    public String deleteRisk(@ModelAttribute Risk risk, @RequestParam(value = "id", required=true) Long id) {
        riskService.deleteRisk(id);
        return "redirect:/admin/manageRisks";
    }

    @GetMapping(value = "/admin/filterLogs")
    public String filterLogs(Model model)
    {
        model.addAttribute("UserFiltering",false);
        model.addAttribute("Title","FilterLogs");
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("logObject", new LogFilterBackingObject());
        model.addAttribute("postUrl", "/admin/filterLogs");
        return "logging/logFilter";
    }
    @PostMapping(value = "/admin/filterLogs")
    public String filterLogsPost(@ModelAttribute("logObject") LogFilterBackingObject lo, @Valid @ModelAttribute("usercore") UserFormBackingObject user, BindingResult result, Errors errors, Model model)
    {
        HashMap<Long, UserCore> userCoreHashMap = userService.findMatchesFromFilter(user);

        List<Log> logList = logService.returnMatchingLogs(lo,(userCoreHashMap.keySet()));
        model.addAttribute("MatchedUsersMap",userCoreHashMap);
        model.addAttribute("logList",logList);

        if (lo.getOperation() == null){return "redirect:/admin/displayAllLogs";}
        switch (lo.getOperation())
        {
            case AddUser:
                return "redirect:/admin/FilterAddLogs";

        }
        return "logging/logFilter";
    }

    @GetMapping(value = "admin/displayAllLogs")
    public String displayAllLogs(@SessionAttribute("logList") List<Log> logs, @SessionAttribute("MatchedUsersMap") HashMap<Long,UserCore> users)
    {
        return "/logging/displayAllLogs";
    }

    @GetMapping(value = "admin/FilterAddLogs")
    public String filterAddLogs(Model model)
    {

        model.addAttribute("UserFiltering",true);
        model.addAttribute("Title","Select Created User Details");
        model.addAttribute("usercore", new UserFormBackingObject());
        model.addAttribute("postUrl", "/admin/FilterAddLogs");
        return "/admin/userFilterFields";
    }

    @PostMapping(value = "admin/FilterAddLogs")
    public String FilterAddLogs(@SessionAttribute("logList") List<Log> logs, @ModelAttribute("usercore") UserFormBackingObject user, RedirectAttributes flash)
    {
        List<AddUser> addLogs = new ArrayList<>();
        for (Log l:logs)
        {
            AddUser event = (AddUser)l.returnLoggedOperation();
            if (event.meetsFilter(user)){addLogs.add(event);}
        }
        flash.addFlashAttribute("addLogs",addLogs);
        return "redirect:/admin/DisplayAddLogs";
    }

    @GetMapping(value = "admin/DisplayAddLogs")
    public String DisplayAddLogs(@SessionAttribute("MatchedUsersMap") HashMap<Long, UserCore> matchedUsers,@ModelAttribute("addLogs")List<AddUser> addLogs, Model model)
    {
        return "logging/AddUser/DisplayAddUserLogs";
    }
}
UI TODO

General
- Implement resizing for smaller screens (unimportant for now)
- Sort out why checkboxes won't stay in place

misc/login
- Incorrect username/password message

fragments/header
- Highlight current page in header bar (patients, create handover etc)

misc/home
- Change table layout to make more sense of filtering and sorting, e.g. one column for name and be able to click sort by name, one column for wards and be able to filter by ward etc
- After filters have been applied, make it more clear to the user how to see all patients again

misc/printJobs

user/settings
- Some confirmation of password being successfully changed

admin/admin
- Make page look a bit nicer, add icons to the links or something?

admin/createUser
- Separate edit user into separate page and remove the password boxes
- Checkboxes not behaving

admin/editUserSelect

admin/manageCategories
- Currently can add categories with empty name
- List the existing categories in alphabetical order for ease to user
- When you click 'edit' have a 'cancel' button as well as 'submit'
- Currently we cannot include 'delete' as there may be entries in the database already using it, how to handle this?

admin/manageRisks
- Currently can add categories with empty name
- List the existing risks in alphabetical order for ease to user
- When you click 'edit' have a 'cancel' button as well as 'submit'

admin/manageWards
- Currently can add wards with empty name
- List the existing wards in alphabetical order for ease to user
- When you click 'edit' have a 'cancel' button as well as 'submit'
- Currently we cannot include 'delete' as there may be entries in the database already using it, how to handle this?

admin/userFilterFields

doctor/viewPatient
- Whole page needs implementing

handover/newPatient
- Sort out 'switch to existing patients' button
- Add validation/error messages

handover/searchPatient
- Decide where to put 'switch to new patient' button
- Add searching by hospital number
- Add searching by NHS number
- Add searching by date of birth

handover/choosePatient

handover/jobContext
- Add risk functionality
- Implement edit patient button in same style as editing wards/categories/risks
- Sort out existing vs new job context buttons/div displays

doctor/handoverJob
doctor/newJob
handover/chooseJob
- Combine these three pages so that when you click 'handover' or 'new job' you see the handover/chooseJob layout, with patient details and job context details
- Implement edit patient and edit job context button in same style as editing wards/categories/risks

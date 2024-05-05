const EditController = {
    get mode() {
        let editMode = localStorage.getItem("editMode");

        let user = JSON.parse(localStorage.getItem('user'));
        if (user == null) {
            return false;
        }

        if(!user.is_admin){
            return false;
        }

        return editMode == "true";
    },

    set mode(isOn) {
        localStorage.setItem("editMode", String(isOn));
    }
}

export default EditController;
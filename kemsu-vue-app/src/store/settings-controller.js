const SettingsController = {
    getMode(item, requireAdmin) {
        let editMode = localStorage.getItem(item);

        if (requireAdmin) {
            let user = JSON.parse(localStorage.getItem('user'));
            if (user == null) {
                return false;
            }

            if (!user.is_admin) {
                return false;
            }
        }

        return editMode == "true";
    },

    setMode(item, isOn) {
        console.log(item, isOn);
        localStorage.setItem(item, String(isOn));
    },

    getEditMode() {
        return this.getMode("editMode", true);
    },

    setEditMode(isOn) {
        this.setMode("editMode", isOn);
    },

    getDarkTheme() {
        return this.getMode("darkTheme", false);
    },

    setDarkTheme(isOn) {
        this.setMode("darkTheme", isOn);
    },

    getFontSize() {
        return this.getMode("fontSize", false);
    },

    setFontSize(isOn) {
        this.setMode("fontSize", isOn);
    },

    switchFontSize() {
        let app = document.querySelector("#app");
        if (this.getFontSize()) {            
            app.classList.add("large-font");
        }
        else{
            app.classList.add("small-font");
        }
    },

    switchTheme() {
        if (this.getDarkTheme()) {
            let app = document.querySelector("#app");
            app.classList.add("dark", "text-white");

            let tables = document.querySelectorAll(".table");
            console.log(tables);
            tables.forEach(element => {
                element.classList.add("table-dark");
            });

            let buttons = document.querySelectorAll(".btn");
            buttons.forEach(element => {
                element.classList.replace("btn-dark", "btn-outline-light");
                element.classList.replace("btn-danger", "btn-outline-danger");
                element.classList.replace("btn-info", "btn-outline-info");
                element.classList.replace("btn-success", "btn-outline-success");
                element.classList.replace("btn-primary", "btn-outline-primary");
            })
        }
    },

    switchAll(){
        this.switchFontSize();
        this.switchTheme();
    }
}

export default SettingsController;
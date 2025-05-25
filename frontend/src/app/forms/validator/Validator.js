export default class Validator {
    constructor(fields) {
        this.fields = fields;
        this.clearError();
    }
     validateFields(field) {
        if (field.value.trim() === "") {
            this.showFieldError(
                field,
                `${field.nextElementSibling.innerText} cannot be blank`,
            );
            return false;
        } else {
            if (field.type === "password") {
                if (field.value.length < 3) {
                    this.showFieldError(
                        field,
                        `${field.nextElementSibling.innerText} must be at least 8 characters`,
                    );
                    return false;
                } else {
                    this.clearError(field, null);
                    return true;
                }
            } else {
                this.clearError(field, null);
                return true;
            }
        }
    }

    findFieldByClassName(){
        let input
        this.fields.forEach((field) => {
            input = document.querySelector(`#${field}`);
        });
        return input;
    }
    retrieveFieldErrors(){
        let errorCount = 0;
        const input = this.findFieldByClassName();
        if (!this.validateFields(input)) {
            errorCount++;
        }
        return errorCount;
    }


    showFieldError(field, message) {
        const errorMessage = field.parentElement.querySelector(".error-message");
        errorMessage.innerText = message;
        field.classList.add("error");
        this.showErrorBox(message)
    }

    clearError(field) {
        let errorMessage;
        if(field !== undefined) {
            errorMessage = field.parentElement.querySelector("span.error-message");
        }else{
            if(this.fields !== undefined) {
                field = this.findFieldByClassName(this.fields);
                errorMessage = field.parentElement.querySelector("span.error-message");
            }
        }
       const errorMessageBox = document.querySelectorAll(".response-error-wrapper");
        if(errorMessage) {
            errorMessage.innerText = "";
            field.classList.remove("error");
        }
        if(errorMessageBox){
            errorMessageBox.forEach(message => {
                message.innerHTML = "";
            });
        }
    }

    showErrorBox(error) {
        if (error) {
            const errorMessage = document.querySelectorAll(".response-error-wrapper");
            errorMessage.forEach(message => {
                message.innerHTML = `
               <div class="response-error-container">
                   <div class="error-circle">
                       <i class="fa-solid fa-circle-exclamation"></i>
                   </div>
                   <span class="response-error-message">
                       <ul>
                           <li>${error}</li>
                       </ul>
                   </span>
               </div>
                `;
            });
        }
    }

}

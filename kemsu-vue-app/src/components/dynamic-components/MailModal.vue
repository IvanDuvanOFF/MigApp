<template>
    <div class="modal fade text-black" id="mailModal" @focus="showRecipient" tabindex="-1" aria-labelledby="mailModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="mailModalLabel">{{ $t("mail-modal.label") }}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="recipient-name" class="col-form-label">{{ $t("mail-modal.to") }}</label>
                            <input type="text" v-model="recipient" disabled 
                                :class="[{'text-showing': this.showed}, 'form-control']" id="recipient-name">
                        </div>
                        <div class="form-group">
                            <label for="message-text" class="col-form-label">{{ $t("mail-modal.message") }}</label>
                            <textarea v-model="message" class="form-control" id="message-text"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">{{ $t("mail-modal.close") }}</button>
                    <button type="button" @click="sendMail" class="btn btn-primary">{{ $t("mail-modal.send") }}</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: ['email'],
    name: "MailModal",    
    data() {        
        return {
            recipient: this.email,
            message: "",
            showed: false
        }
    },    
    methods: {
        showRecipient(){
            this.showed = true;
            this.recipient = this.email
        },
        sendMail() {
            console.log(this.email);
            console.log(this.message);
        }
    }
}
</script>

<style>
.text-showing {
  animation-duration: 2s;
  animation-name: slidein; 
}

@keyframes slidein {
  from {
    color: #0000;    
  }

  to {
    color: black;
  }
}
</style>
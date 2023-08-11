import { createApp, h, ref } from 'vue';
import XDialog from '@/xui/XDialog.vue';

let isShowDialog = false;
let modalContainer = null;
let app = null;
export function showDialog(title, btnName, content, s, confirmFunc, closeFunc) {
  if (isShowDialog) {
    return;
  }
  isShowDialog = true;
  modalContainer = document.createElement('div');
  document.body.appendChild(modalContainer);
  app = createApp({
    setup() {
      const xDialogRef = ref(null);
      return { xDialogRef };
    },
    render() {
      return h(XDialog, {
        ref: 'xDialogRef',
        onBtnClick: async () => {
          try {
            confirmFunc && (await confirmFunc());
          } finally {
            closeDialog();
          }
        },
        onClose: async () => {
          try {
            closeFunc && (await closeFunc());
          } finally {
            closeDialog();
          }
        }
      });
    }
  });
  const instance = app.mount(modalContainer);
  instance.$refs.xDialogRef.openModal(title, btnName, content, s);
}

export function closeDialog() {
  if (isShowDialog) {
    app.unmount(modalContainer);
    document.body.removeChild(modalContainer);
    isShowDialog = false;
  }
}

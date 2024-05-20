class ApiUrls {
  static String apiHost = 'https://cute-example-deeplom.ru';
  static String signIn = '$apiHost/api/auth/signing';
  static String tfa = '$apiHost/api/auth/tfa';
  static String restore = '$apiHost/api/auth/restore';
  static String verifyPass = '$apiHost/api/auth/restore/verify';
  static String restorePass = '$apiHost/api/auth/restore/sending_option';
  static String refreshToken = '$apiHost/api/auth/refresh';
  static String changePass = '$apiHost/api/student';
  static String logout = '$apiHost/api/logout';
  static String studentList = '$apiHost/api/students';
  static String mainScreen = '$apiHost/api/main_screen';
  static String application = '$apiHost/api/applications';
  static String notifications = '$apiHost/api/notifications';
  static String profile = '$apiHost/api/profile';
  static String remember = '$apiHost/api/remember';
  static String uploadFile = '$apiHost/api/files/upload';
}

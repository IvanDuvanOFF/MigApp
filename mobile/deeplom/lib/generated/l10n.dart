// GENERATED CODE - DO NOT MODIFY BY HAND
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'intl/messages_all.dart';

// **************************************************************************
// Generator: Flutter Intl IDE plugin
// Made by Localizely
// **************************************************************************

// ignore_for_file: non_constant_identifier_names, lines_longer_than_80_chars
// ignore_for_file: join_return_with_assignment, prefer_final_in_for_each
// ignore_for_file: avoid_redundant_argument_values, avoid_escaping_inner_quotes

class S {
  S();

  static S? _current;

  static S get current {
    assert(_current != null,
        'No instance of S was loaded. Try to initialize the S delegate before accessing S.current.');
    return _current!;
  }

  static const AppLocalizationDelegate delegate = AppLocalizationDelegate();

  static Future<S> load(Locale locale) {
    final name = (locale.countryCode?.isEmpty ?? false)
        ? locale.languageCode
        : locale.toString();
    final localeName = Intl.canonicalizedLocale(name);
    return initializeMessages(localeName).then((_) {
      Intl.defaultLocale = localeName;
      final instance = S();
      S._current = instance;

      return instance;
    });
  }

  static S of(BuildContext context) {
    final instance = S.maybeOf(context);
    assert(instance != null,
        'No instance of S present in the widget tree. Did you add S.delegate in localizationsDelegates?');
    return instance!;
  }

  static S? maybeOf(BuildContext context) {
    return Localizations.of<S>(context, S);
  }

  /// `Entry`
  String get entryText {
    return Intl.message(
      'Entry',
      name: 'entryText',
      desc: '',
      args: [],
    );
  }

  /// `Login / email`
  String get loginEmailText {
    return Intl.message(
      'Login / email',
      name: 'loginEmailText',
      desc: '',
      args: [],
    );
  }

  /// `login`
  String get loginHint {
    return Intl.message(
      'login',
      name: 'loginHint',
      desc: '',
      args: [],
    );
  }

  /// `Please fill in the field`
  String get needFillAllFields {
    return Intl.message(
      'Please fill in the field',
      name: 'needFillAllFields',
      desc: '',
      args: [],
    );
  }

  /// `Password`
  String get password {
    return Intl.message(
      'Password',
      name: 'password',
      desc: '',
      args: [],
    );
  }

  /// `Please fill in the field`
  String get pleaseFillFieldText {
    return Intl.message(
      'Please fill in the field',
      name: 'pleaseFillFieldText',
      desc: '',
      args: [],
    );
  }

  /// `Enter`
  String get enterText {
    return Intl.message(
      'Enter',
      name: 'enterText',
      desc: '',
      args: [],
    );
  }

  /// `I don't remember the password`
  String get rememberPassword {
    return Intl.message(
      'I don\'t remember the password',
      name: 'rememberPassword',
      desc: '',
      args: [],
    );
  }

  /// `There are no available applications at the moment`
  String get noAvailableApplicationText {
    return Intl.message(
      'There are no available applications at the moment',
      name: 'noAvailableApplicationText',
      desc: '',
      args: [],
    );
  }

  /// `Wow! All the documents will be here soon...`
  String get documentsWillBeHereSoonText {
    return Intl.message(
      'Wow! All the documents will be here soon...',
      name: 'documentsWillBeHereSoonText',
      desc: '',
      args: [],
    );
  }

  /// `Select the file source`
  String get selectSourceFile {
    return Intl.message(
      'Select the file source',
      name: 'selectSourceFile',
      desc: '',
      args: [],
    );
  }

  /// `Choose from the gallery`
  String get choiceFromGallery {
    return Intl.message(
      'Choose from the gallery',
      name: 'choiceFromGallery',
      desc: '',
      args: [],
    );
  }

  /// `Take a picture`
  String get takePictureText {
    return Intl.message(
      'Take a picture',
      name: 'takePictureText',
      desc: '',
      args: [],
    );
  }

  /// `Select from device`
  String get choiceFromDeviceText {
    return Intl.message(
      'Select from device',
      name: 'choiceFromDeviceText',
      desc: '',
      args: [],
    );
  }

  /// `Next`
  String get nextText {
    return Intl.message(
      'Next',
      name: 'nextText',
      desc: '',
      args: [],
    );
  }

  /// `By E-mail`
  String get byEmailText {
    return Intl.message(
      'By E-mail',
      name: 'byEmailText',
      desc: '',
      args: [],
    );
  }

  /// `By SMS`
  String get bySmsText {
    return Intl.message(
      'By SMS',
      name: 'bySmsText',
      desc: '',
      args: [],
    );
  }

  /// `The file is uploaded`
  String get fileUpload {
    return Intl.message(
      'The file is uploaded',
      name: 'fileUpload',
      desc: '',
      args: [],
    );
  }

  /// `Ok`
  String get okText {
    return Intl.message(
      'Ok',
      name: 'okText',
      desc: '',
      args: [],
    );
  }

  /// `Go to a chat with an employee`
  String get goToEmployeeChat {
    return Intl.message(
      'Go to a chat with an employee',
      name: 'goToEmployeeChat',
      desc: '',
      args: [],
    );
  }

  /// `Student's Memo`
  String get studentFileText {
    return Intl.message(
      'Student\'s Memo',
      name: 'studentFileText',
      desc: '',
      args: [],
    );
  }

  /// `Applications`
  String get applicationsText {
    return Intl.message(
      'Applications',
      name: 'applicationsText',
      desc: '',
      args: [],
    );
  }

  /// `The applications submitted by you will be displayed here`
  String get applicationDisplayedHereText {
    return Intl.message(
      'The applications submitted by you will be displayed here',
      name: 'applicationDisplayedHereText',
      desc: '',
      args: [],
    );
  }

  /// `mon`
  String get monday {
    return Intl.message(
      'mon',
      name: 'monday',
      desc: '',
      args: [],
    );
  }

  /// `tue`
  String get tuesday {
    return Intl.message(
      'tue',
      name: 'tuesday',
      desc: '',
      args: [],
    );
  }

  /// `wed`
  String get wednesday {
    return Intl.message(
      'wed',
      name: 'wednesday',
      desc: '',
      args: [],
    );
  }

  /// `thu`
  String get thursday {
    return Intl.message(
      'thu',
      name: 'thursday',
      desc: '',
      args: [],
    );
  }

  /// `fri`
  String get friday {
    return Intl.message(
      'fri',
      name: 'friday',
      desc: '',
      args: [],
    );
  }

  /// `sat`
  String get saturday {
    return Intl.message(
      'sat',
      name: 'saturday',
      desc: '',
      args: [],
    );
  }

  /// `sun`
  String get sunday {
    return Intl.message(
      'sun',
      name: 'sunday',
      desc: '',
      args: [],
    );
  }

  /// `Notifications`
  String get notifications {
    return Intl.message(
      'Notifications',
      name: 'notifications',
      desc: '',
      args: [],
    );
  }

  /// `There are no notifications`
  String get noNotificationsText {
    return Intl.message(
      'There are no notifications',
      name: 'noNotificationsText',
      desc: '',
      args: [],
    );
  }

  /// `Exit`
  String get exit {
    return Intl.message(
      'Exit',
      name: 'exit',
      desc: '',
      args: [],
    );
  }

  /// `FIO`
  String get fio {
    return Intl.message(
      'FIO',
      name: 'fio',
      desc: '',
      args: [],
    );
  }

  /// `Age`
  String get age {
    return Intl.message(
      'Age',
      name: 'age',
      desc: '',
      args: [],
    );
  }

  /// `Institute, group`
  String get instituteGroup {
    return Intl.message(
      'Institute, group',
      name: 'instituteGroup',
      desc: '',
      args: [],
    );
  }

  /// `Old password`
  String get oldPasswordText {
    return Intl.message(
      'Old password',
      name: 'oldPasswordText',
      desc: '',
      args: [],
    );
  }

  /// `New password`
  String get nextPassword {
    return Intl.message(
      'New password',
      name: 'nextPassword',
      desc: '',
      args: [],
    );
  }

  /// `Save`
  String get saveText {
    return Intl.message(
      'Save',
      name: 'saveText',
      desc: '',
      args: [],
    );
  }

  /// `Settings`
  String get settings {
    return Intl.message(
      'Settings',
      name: 'settings',
      desc: '',
      args: [],
    );
  }

  /// `This is your status in the system`
  String get userStatusDescription {
    return Intl.message(
      'This is your status in the system',
      name: 'userStatusDescription',
      desc: '',
      args: [],
    );
  }

  /// `Contact information`
  String get contactData {
    return Intl.message(
      'Contact information',
      name: 'contactData',
      desc: '',
      args: [],
    );
  }

  /// `Email`
  String get email {
    return Intl.message(
      'Email',
      name: 'email',
      desc: '',
      args: [],
    );
  }

  /// `Phone`
  String get phone {
    return Intl.message(
      'Phone',
      name: 'phone',
      desc: '',
      args: [],
    );
  }

  /// `years old`
  String get ages {
    return Intl.message(
      'years old',
      name: 'ages',
      desc: '',
      args: [],
    );
  }

  /// `years`
  String get years {
    return Intl.message(
      'years',
      name: 'years',
      desc: '',
      args: [],
    );
  }

  /// `year`
  String get year {
    return Intl.message(
      'year',
      name: 'year',
      desc: '',
      args: [],
    );
  }

  /// `The password must contain at least 8 characters, including at least one digit, one uppercase and one lowercase letter.`
  String get passwordRequired {
    return Intl.message(
      'The password must contain at least 8 characters, including at least one digit, one uppercase and one lowercase letter.',
      name: 'passwordRequired',
      desc: '',
      args: [],
    );
  }

  /// `Show all applications`
  String get showAllApplication {
    return Intl.message(
      'Show all applications',
      name: 'showAllApplication',
      desc: '',
      args: [],
    );
  }

  /// `We have sent you a code`
  String get codeSendText {
    return Intl.message(
      'We have sent you a code',
      name: 'codeSendText',
      desc: '',
      args: [],
    );
  }

  /// `The code will be sent to `
  String get codeRecieveOnText {
    return Intl.message(
      'The code will be sent to ',
      name: 'codeRecieveOnText',
      desc: '',
      args: [],
    );
  }

  /// `OTP code`
  String get otpText {
    return Intl.message(
      'OTP code',
      name: 'otpText',
      desc: '',
      args: [],
    );
  }

  /// `Password successfully changed`
  String get passwordChangeSuccess {
    return Intl.message(
      'Password successfully changed',
      name: 'passwordChangeSuccess',
      desc: '',
      args: [],
    );
  }
}

class AppLocalizationDelegate extends LocalizationsDelegate<S> {
  const AppLocalizationDelegate();

  List<Locale> get supportedLocales {
    return const <Locale>[
      Locale.fromSubtags(languageCode: 'en'),
      Locale.fromSubtags(languageCode: 'ru'),
    ];
  }

  @override
  bool isSupported(Locale locale) => _isSupported(locale);
  @override
  Future<S> load(Locale locale) => S.load(locale);
  @override
  bool shouldReload(AppLocalizationDelegate old) => false;

  bool _isSupported(Locale locale) {
    for (var supportedLocale in supportedLocales) {
      if (supportedLocale.languageCode == locale.languageCode) {
        return true;
      }
    }
    return false;
  }
}

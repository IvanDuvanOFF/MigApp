import 'package:equatable/equatable.dart';

abstract class ProfileEvents extends Equatable {}

class OnInit extends ProfileEvents {
  OnInit();

  @override
  List<Object> get props => [];
}

class ChangePassword extends ProfileEvents {
  ChangePassword({required this.oldPassword, required this.newPassword});

  final String oldPassword;
  final String newPassword;

  @override
  List<Object> get props => [oldPassword, newPassword];
}

class ResetPassword extends ProfileEvents {
  ResetPassword();

  @override
  List<Object> get props => [];
}

class Logout extends ProfileEvents {
  Logout();

  @override
  List<Object> get props => [];
}

class OnOldObscurePass extends ProfileEvents {
  OnOldObscurePass();

  @override
  List<Object> get props => [];
}

class OnNewObscurePass extends ProfileEvents {
  OnNewObscurePass();

  @override
  List<Object> get props => [];
}

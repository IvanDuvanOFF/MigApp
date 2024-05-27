import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/profile_model.dart';
import 'package:equatable/equatable.dart';

class ProfileState extends Equatable {
  final Lce<ProfileModel> profile;
  final bool oldPassIsObscure;
  final bool newPassIsObscure;

  List<DateTime> get currentMonthDates => List.generate(
        DateTime(DateTime.now().year, DateTime.now().month + 1, 0).day,
        (index) => DateTime(DateTime.now().year, DateTime.now().month, index + 1),
      );

  const ProfileState({
    this.oldPassIsObscure = true,
    this.newPassIsObscure = true,
    this.profile = const Lce.loading(),
  });

  ProfileState copyWith({
    Lce<ProfileModel>? profile,
    bool? oldPassIsObscure,
    bool? newPassIsObscure,
  }) {
    return ProfileState(
      profile: profile ?? this.profile,
      oldPassIsObscure: oldPassIsObscure ?? this.oldPassIsObscure,
      newPassIsObscure: newPassIsObscure ?? this.newPassIsObscure,
    );
  }

  @override
  List<Object?> get props => [profile, oldPassIsObscure, newPassIsObscure];
}

import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/generated/l10n.dart';
import 'package:deeplom/screens/add_application/add_application_screen.dart';

import 'package:deeplom/screens/profile/profile_bloc.dart';
import 'package:deeplom/screens/profile/profile_events.dart';
import 'package:deeplom/screens/profile/profile_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:get_it/get_it.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  TextEditingController oldPassword = TextEditingController();
  TextEditingController newPassword = TextEditingController();
  final _profileBloc = ProfileBloc(
    const ProfileState(),
    mainRepository: GetIt.I<AbstractMainRepository>(),
  );

  @override
  void initState() {
    _profileBloc.add(OnInit());
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusScope.of(context).unfocus(),
      child: Scaffold(
        extendBodyBehindAppBar: true,
        body: SafeArea(
          child: BlocBuilder<ProfileBloc, ProfileState>(
            bloc: _profileBloc,
            builder: (context, state) {
              if (state.profile.isLoading) {
                return const Center(child: CircularProgressIndicator());
              }

              return Padding(
                padding: const EdgeInsets.symmetric(horizontal: 18.0, vertical: 12.0),
                child: SingleChildScrollView(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisSize: MainAxisSize.max,
                    children: [
                      Align(
                        alignment: Alignment.topRight,
                        child: InkWell(
                          onTap: () => _profileBloc.add(Logout()),
                          child: Container(
                            decoration: BoxDecoration(
                              color: Colors.grey.withOpacity(.6),
                              borderRadius: BorderRadius.circular(12.0),
                            ),
                            child: Padding(
                              padding: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
                              child: Text(S.of(context).exit),
                            ),
                          ),
                        ),
                      ),
                      Stack(
                        alignment: Alignment.topRight,
                        children: [
                          Container(
                            height: 120.0,
                            width: 120.0,
                            decoration: state.profile.requiredContent.photo.isNotEmpty
                                ? BoxDecoration(
                                    image: DecorationImage(image: MemoryImage(state.avatar!), fit: BoxFit.cover),
                                    color: Colors.grey.withOpacity(.6),
                                    borderRadius: BorderRadius.circular(120.0),
                                  )
                                : BoxDecoration(
                                    color: Colors.grey.withOpacity(.6),
                                    borderRadius: BorderRadius.circular(120.0),
                                  ),
                            child: state.profile.requiredContent.photo.isNotEmpty
                                ? const SizedBox()
                                : const Center(
                                    child: Icon(
                                      Icons.person,
                                      size: 32.0,
                                    ),
                                  ),
                          ),
                          InkWell(
                            onTap: () => showAvatarDialog(context),
                            child: Container(
                              height: 24.0,
                              width: 24.0,
                              decoration: BoxDecoration(
                                color: Colors.black.withOpacity(.6),
                                borderRadius: BorderRadius.circular(120.0),
                              ),
                              child: const Center(
                                child: Icon(
                                  Icons.edit,
                                  color: Colors.white,
                                  size: 16.0,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(
                        height: 12.0,
                      ),
                      Stack(
                        alignment: Alignment.topRight,
                        children: [
                          if (state.profile.requiredContent.status.isNotEmpty && state.profile.requiredContent.status != 'NONE') ...[
                            Container(
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(8.0),
                                border: Border.all(color: Colors.grey),
                              ),
                              child: Padding(
                                padding: const EdgeInsets.symmetric(horizontal: 32.0, vertical: 8.0),
                                child: Text(
                                  state.profile.requiredContent.status,
                                  style: const TextStyle(fontSize: 16.0),
                                ),
                              ),
                            ),
                            InkWell(
                              onTap: () => showStatusDialog(context),
                              child: Container(
                                height: 18.0,
                                width: 18.0,
                                decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(24.0),
                                  border: Border.all(color: Colors.grey),
                                ),
                                child: const Center(
                                  child: Icon(
                                    Icons.question_mark,
                                    color: Colors.grey,
                                    size: 16.0,
                                  ),
                                ),
                              ),
                            ),
                          ]
                        ],
                      ),
                      const SizedBox(height: 24.0),
                      Container(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(12.0),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withOpacity(0.3),
                              blurRadius: 2.0,
                              offset: const Offset(1, 1),
                            ),
                          ],
                        ),
                        child: Padding(
                          padding: const EdgeInsets.all(12.0),
                          child: Column(
                            children: [
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(
                                    S.of(context).fio,
                                    style: const TextStyle(fontWeight: FontWeight.bold),
                                  ),
                                  Text(
                                      '${state.profile.requiredContent.surname} ${state.profile.requiredContent.name} ${state.profile.requiredContent.patronymic}'),
                                ],
                              ),
                              const Divider(),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(
                                    S.of(context).age,
                                    style: const TextStyle(fontWeight: FontWeight.bold),
                                  ),
                                  Text(formatAge(DateTime.now().year - state.profile.requiredContent.birthday.year)),
                                ],
                              ),
                              const Divider(),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(
                                    S.of(context).instituteGroup,
                                    style: const TextStyle(fontWeight: FontWeight.bold),
                                  ),
                                  // if (state.profile.requiredContent.institute.isNotEmpty || state.profile.requiredContent.group.isNotEmpty)
                                  Text(
                                      '${state.profile.requiredContent.institute.isEmpty ? '-' : state.profile.requiredContent.institute}, ${state.profile.requiredContent.group.isEmpty ? '-' : state.profile.requiredContent.group}'),
                                ],
                              ),
                              const Divider(),
                            ],
                          ),
                        ),
                      ),
                      const SizedBox(height: 24.0),
                      Container(
                        decoration: BoxDecoration(
                          color: Colors.grey.withOpacity(.1),
                          borderRadius: BorderRadius.circular(12.0),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.all(12.0),
                          child: Column(
                            mainAxisSize: MainAxisSize.max,
                            children: [
                              Text(
                                S.of(context).password,
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                              ),
                              const SizedBox(height: 12.0),
                              Row(
                                children: [
                                  Text(S.of(context).oldPasswordText),
                                  const SizedBox(width: 6.0),
                                  Expanded(
                                    child: TextField(
                                      controller: oldPassword,
                                      obscureText: state.oldPassIsObscure,
                                      decoration: const InputDecoration(border: OutlineInputBorder()),
                                    ),
                                  ),
                                  IconButton(
                                      onPressed: () => _profileBloc.add(OnOldObscurePass()),
                                      icon: Icon(state.oldPassIsObscure ? Icons.remove_red_eye_outlined : Icons.remove_red_eye))
                                ],
                              ),
                              const SizedBox(height: 12.0),
                              Row(
                                children: [
                                  Text(S.of(context).nextPassword),
                                  const SizedBox(width: 6.0),
                                  Expanded(
                                    child: TextField(
                                      controller: newPassword,
                                      obscureText: state.newPassIsObscure,
                                      decoration: const InputDecoration(border: OutlineInputBorder()),
                                    ),
                                  ),
                                  IconButton(
                                      onPressed: () => _profileBloc.add(OnNewObscurePass()),
                                      icon: Icon(state.newPassIsObscure ? Icons.remove_red_eye_outlined : Icons.remove_red_eye))
                                ],
                              ),
                              const SizedBox(height: 12.0),
                              Text(
                                S.of(context).passwordRequired,
                                style: const TextStyle(fontSize: 12.0),
                              ),
                              if (state.passChangeSuccess) ...[
                                const SizedBox(height: 12.0),
                                Text(
                                  S.of(context).passwordChangeSuccess,
                                  style: const TextStyle(color: Colors.green, fontWeight: FontWeight.bold, fontSize: 16.0),
                                ),
                              ],
                              const SizedBox(height: 12.0),
                              if (state.error.isNotEmpty)
                                Text(
                                  state.error,
                                  style: const TextStyle(color: Colors.red),
                                ),
                              const SizedBox(height: 12.0),
                              Padding(
                                padding: const EdgeInsets.symmetric(horizontal: 28.0),
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    ElevatedButton(
                                      style: ElevatedButton.styleFrom(backgroundColor: Colors.white, foregroundColor: Colors.black),
                                      onPressed: () => _profileBloc.add(ChangePassword(oldPassword: oldPassword.text, newPassword: newPassword.text)),
                                      child: Text(S.of(context).saveText),
                                    ),
                                    TextButton(
                                      onPressed: () => AppRouting.toResetPassword(),
                                      child: Text(
                                        S.of(context).rememberPassword,
                                        style: const TextStyle(
                                          color: Colors.black,
                                          decoration: TextDecoration.underline,
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      const SizedBox(height: 24.0),
                      Container(
                        decoration: BoxDecoration(
                          color: Colors.grey.withOpacity(.1),
                          borderRadius: BorderRadius.circular(12.0),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.all(12.0),
                          child: Column(
                            mainAxisSize: MainAxisSize.min,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              Text(
                                S.of(context).contactData,
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                              ),
                              const SizedBox(height: 12.0),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(
                                    S.of(context).email,
                                    style: const TextStyle(fontWeight: FontWeight.bold),
                                  ),
                                  const SizedBox(width: 6.0),
                                  Text(state.profile.requiredContent.email.isEmpty ? '-' : state.profile.requiredContent.email),
                                ],
                              ),
                              const SizedBox(height: 12.0),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(
                                    S.of(context).phone,
                                    style: const TextStyle(fontWeight: FontWeight.bold),
                                  ),
                                  const SizedBox(width: 6.0),
                                  Text(state.profile.requiredContent.phoneNumber.isEmpty ? '-' : state.profile.requiredContent.phoneNumber),
                                ],
                              ),
                            ],
                          ),
                        ),
                      )
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ),
    );
  }

  showStatusDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            content: SizedBox(
              height: 50,
              width: 100,
              child: Center(
                child: Text(S.of(context).userStatusDescription),
              ),
            ),
          );
        });
  }

  void showAvatarDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(S.of(context).selectSourceFile),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              ListTile(
                leading: const Icon(Icons.photo_library),
                title: Text(S.of(context).choiceFromGallery),
                onTap: () {
                  Navigator.of(context).pop();
                  _profileBloc.add(ChoiceAvatar(FileSourceType.gallery));
                },
              ),
              ListTile(
                leading: const Icon(Icons.camera_alt),
                title: Text(S.of(context).takePictureText),
                onTap: () {
                  Navigator.of(context).pop();
                  _profileBloc.add(ChoiceAvatar(FileSourceType.camera));
                },
              ),
              ListTile(
                leading: const Icon(Icons.attach_file),
                title: Text(S.of(context).choiceFromDeviceText),
                onTap: () {
                  Navigator.of(context).pop();
                  _profileBloc.add(ChoiceAvatar(FileSourceType.device));
                },
              ),
            ],
          ),
        );
      },
    );
  }

  String formatAge(int age) {
    if (age % 10 == 1 && age % 100 != 11) {
      return '$age ${S.of(context).year}';
    } else if (age % 10 >= 2 && age % 10 <= 4 && (age % 100 < 10 || age % 100 >= 20)) {
      return '$age ${S.of(context).ages}';
    } else {
      return '$age ${S.of(context).years}';
    }
  }
}

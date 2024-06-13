import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/auth/abstract_auth_repository.dart';
import 'package:deeplom/generated/l10n.dart';
import 'package:deeplom/screens/auth/auth_bloc.dart';
import 'package:deeplom/screens/auth/auth_events.dart';
import 'package:deeplom/screens/auth/auth_state.dart';
import 'package:deeplom/screens/widgets/dev_mode.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:get_it/get_it.dart';

class AuthScreen extends StatefulWidget {
  const AuthScreen({super.key});

  @override
  State<AuthScreen> createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  final _authBloc = AuthBloc(
    const AuthState(),
    authRepository: GetIt.I<AbstractAuthRepository>(),
  );
  TextEditingController loginController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  void initState() {
    _authBloc.add(OnInit());
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusScope.of(context).unfocus(),
      child: Scaffold(
        // resizeToAvoidBottomInset: false,
        body: BlocBuilder<AuthBloc, AuthState>(
          bloc: _authBloc,
          builder: (context, state) {
            return SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.end,
                  crossAxisAlignment: CrossAxisAlignment.end,
                  mainAxisSize: MainAxisSize.max,
                  children: [
                    // Padding(
                    //   padding: const EdgeInsets.only(top: 40.0),
                    //   child: Align(
                    //     alignment: Alignment.topRight,
                    //     child: PopupMenuButton<Locale>(
                    //       child: Container(
                    //         decoration: BoxDecoration(
                    //           color: Colors.indigo,
                    //           borderRadius: BorderRadius.circular(8.0),
                    //         ),
                    //         child: Padding(
                    //           padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                    //           child: Text(
                    //             state.selectedLang.languageCode,
                    //             style: const TextStyle(
                    //               color: Colors.white,
                    //               fontWeight: FontWeight.bold,
                    //             ),
                    //           ),
                    //         ),
                    //       ),
                    //       onSelected: (Locale result) => _authBloc.add(ChangeLanguage(locale: Locale(result.languageCode))),
                    //       itemBuilder: (BuildContext context) => <PopupMenuEntry<Locale>>[
                    //         PopupMenuItem<Locale>(
                    //           value: const Locale('ru'),
                    //           child: Row(
                    //             children: [
                    //               LanguageCode.ru.flag,
                    //               const SizedBox(width: 8),
                    //               Text(LanguageCode.ru.name),
                    //             ],
                    //           ),
                    //         ),
                    //         PopupMenuItem<Locale>(
                    //           value: const Locale('en'),
                    //           child: Row(
                    //             children: [
                    //               LanguageCode.en.flag,
                    //               const SizedBox(width: 8),
                    //               Text(LanguageCode.en.name),
                    //             ],
                    //           ),
                    //         ),
                    //       ],
                    //     ),
                    //   ),
                    // ),
                    SizedBox(height: MediaQuery.of(context).size.height / 8),
                    Container(
                      padding: const EdgeInsets.all(16.0),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(32.0),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withOpacity(0.3),
                            blurRadius: 2.0,
                            offset: const Offset(1, 1),
                          ),
                        ],
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Center(
                            child: GestureDetector(
                              onLongPress: () {
                                showDevAlert(context);
                              },
                              child: Text(
                                S.of(context).entryText,
                                style: const TextStyle(fontSize: 24),
                              ),
                            ),
                          ),
                          const SizedBox(height: 16),
                          Text(
                            S.of(context).loginEmailText,
                            style: const TextStyle(fontSize: 16),
                          ),
                          TextField(
                            controller: loginController,
                            decoration: InputDecoration(
                              hintText: S.of(context).loginHint,
                              border: const OutlineInputBorder(),
                              enabledBorder: OutlineInputBorder(
                                borderSide: BorderSide(color: state.error.isNotEmpty && loginController.text.isEmpty ? Colors.red : Colors.grey),
                              ),
                            ),
                          ),
                          if (state.error.isNotEmpty && loginController.text.isEmpty)
                            Text(
                              S.of(context).needFillAllFields,
                              style: const TextStyle(color: Colors.red),
                            ),
                          const SizedBox(height: 16),
                          Text(
                            S.of(context).password,
                            style: const TextStyle(fontSize: 16),
                          ),
                          TextField(
                            controller: passwordController,
                            obscureText: state.passIsObscure,
                            decoration: InputDecoration(
                              hintText: S.of(context).password,
                              border: const OutlineInputBorder(),
                              enabledBorder: OutlineInputBorder(
                                borderSide: BorderSide(color: state.error.isNotEmpty && passwordController.text.isEmpty ? Colors.red : Colors.grey),
                              ),
                              suffixIcon: IconButton(
                                icon: Icon(state.passIsObscure ? Icons.visibility : Icons.visibility_outlined),
                                onPressed: () => _authBloc.add(OnObscurePass()),
                              ),
                            ),
                          ),
                          if (state.error.isNotEmpty && passwordController.text.isEmpty)
                            Text(
                              S.of(context).pleaseFillFieldText,
                              style: const TextStyle(color: Colors.red),
                            ),
                          const SizedBox(height: 16),
                          InkWell(
                            onTap: () => _authBloc.add(OnAuth(login: loginController.text, password: passwordController.text)),
                            child: Container(
                              height: 48.0,
                              width: double.infinity,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.circular(32.0),
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withOpacity(0.3),
                                    blurRadius: 2.0,
                                    offset: const Offset(1, 1),
                                  ),
                                ],
                              ),
                              child: Center(
                                child: Text(
                                  S.of(context).enterText,
                                  style: const TextStyle(fontSize: 16),
                                ),
                              ),
                            ),
                          ),
                          Center(
                            child: TextButton(
                              onPressed: () {
                                AppRouting.toResetPassword();
                              },
                              child: Text(
                                S.of(context).rememberPassword,
                                style: TextStyle(color: Colors.grey.shade600),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 32.0),
                    if (state.error.isNotEmpty)
                      Center(
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
                          decoration: BoxDecoration(
                            color: Colors.grey[800],
                            borderRadius: BorderRadius.circular(30.0),
                          ),
                          child: Text(
                            state.error,
                            style: const TextStyle(color: Colors.white, fontSize: 16.0),
                          ),
                        ),
                      ),
                    const SizedBox(height: 32.0),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}

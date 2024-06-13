import 'package:deeplom/domain/repositories/auth/abstract_auth_repository.dart';
import 'package:deeplom/generated/l10n.dart';
import 'package:deeplom/screens/auth/auth_bloc.dart';
import 'package:deeplom/screens/auth/auth_events.dart';
import 'package:deeplom/screens/auth/auth_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:get_it/get_it.dart';

class ResetPassword extends StatefulWidget {
  const ResetPassword({super.key});

  @override
  State<ResetPassword> createState() => _ResetPasswordState();
}

class _ResetPasswordState extends State<ResetPassword> {
  final _authBloc = AuthBloc(
    const AuthState(),
    authRepository: GetIt.I<AbstractAuthRepository>(),
  );
  TextEditingController typeController = TextEditingController();
  TextEditingController otpCodeController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController passwordConfirmController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusScope.of(context).unfocus(),
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.white,
          elevation: 0,
          iconTheme: const IconThemeData(
            color: Colors.black,
          ),
        ),
        backgroundColor: Colors.white,
        body: SafeArea(
          child: BlocBuilder<AuthBloc, AuthState>(
              bloc: _authBloc,
              builder: (context, state) {
                if (state.isRestoreSending) {
                  return Align(
                    alignment: Alignment.center,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 18.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        mainAxisSize: MainAxisSize.max,
                        children: [
                          Text(S.of(context).codeSendText),
                          Text('${S.of(context).codeRecieveOnText} ${state.phone.isNotEmpty ? state.phone : state.email}'),
                          Padding(
                            padding: const EdgeInsets.all(24.0),
                            child: TextField(
                              controller: otpCodeController,
                              decoration: InputDecoration(
                                hintText: S.of(context).otpText,
                                border: const OutlineInputBorder(),
                                enabledBorder: OutlineInputBorder(
                                  borderSide: BorderSide(color: state.error.isNotEmpty && otpCodeController.text.isEmpty ? Colors.red : Colors.grey),
                                ),
                              ),
                            ),
                          ),
                          InkWell(
                            onTap: () => _authBloc.add(OnRestoreVerify(code: otpCodeController.text)),
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
                          if (state.error.isNotEmpty) ...[
                            const SizedBox(height: 24.0),
                            Text(
                              state.error,
                              style: TextStyle(
                                color: Colors.red,
                                fontSize: 16.0,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ]
                        ],
                      ),
                    ),
                  );
                }

                if (state.isRestoreVerify) {
                  return Align(
                    alignment: Alignment.center,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 18.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        mainAxisSize: MainAxisSize.max,
                        children: [
                          Text('Новый пароль'),
                          Padding(
                            padding: const EdgeInsets.all(24.0),
                            child: TextField(
                              controller: passwordController,
                              decoration: InputDecoration(
                                hintText: 'Новый пароль',
                                border: const OutlineInputBorder(),
                                enabledBorder: OutlineInputBorder(
                                  borderSide: BorderSide(color: state.error.isNotEmpty && otpCodeController.text.isEmpty ? Colors.red : Colors.grey),
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(height: 12.0),
                          Text('Повторите пароль'),
                          Padding(
                            padding: const EdgeInsets.all(24.0),
                            child: TextField(
                              controller: passwordConfirmController,
                              decoration: InputDecoration(
                                hintText: 'Повторите пароль',
                                border: const OutlineInputBorder(),
                                enabledBorder: OutlineInputBorder(
                                  borderSide: BorderSide(color: state.error.isNotEmpty && otpCodeController.text.isEmpty ? Colors.red : Colors.grey),
                                ),
                              ),
                            ),
                          ),
                          InkWell(
                            onTap: () => _authBloc.add(OnUserPasswordRestore(
                              password: passwordController.text,
                              passwordConfirm: passwordConfirmController.text,
                            )),
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
                                  S.of(context).saveText,
                                  style: const TextStyle(fontSize: 16),
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(height: 12.0),
                          if (state.restoreReady)
                            Text(
                              'Пароль успешно установлен',
                              style: TextStyle(
                                color: Colors.green,
                                fontSize: 16.0,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                        ],
                      ),
                    ),
                  );
                }
                return Padding(
                  padding: const EdgeInsets.all(32.0),
                  child: Column(
                    children: [
                      ChoiceMethod(authBloc: _authBloc),
                      const SizedBox(height: 32.0),
                      if (state.resetType != ResetPasswordType.choice)
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            TextField(
                              controller: typeController,
                              decoration: InputDecoration(hintText: state.resetType == ResetPasswordType.email ? 'email' : 'sms'),
                            ),
                            const SizedBox(height: 32.0),
                            InkWell(
                              onTap: () => _authBloc.add(OnResetPassword(resetType: typeController.text)),
                              child: Container(
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
                                child: Padding(
                                  padding: const EdgeInsets.symmetric(horizontal: 32.0, vertical: 16.0),
                                  child: Text(S.of(context).nextText),
                                ),
                              ),
                            )
                          ],
                        ),
                    ],
                  ),
                );
              }),
        ),
      ),
    );
  }
}

class ChoiceMethod extends StatelessWidget {
  const ChoiceMethod({
    super.key,
    required AuthBloc authBloc,
  }) : _authBloc = authBloc;

  final AuthBloc _authBloc;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        InkWell(
          onTap: () => _authBloc.add(ChoiceResetType(type: ResetPasswordType.email)),
          child: Container(
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
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Row(
                children: [
                  const Icon(Icons.email_outlined),
                  const SizedBox(width: 4.0),
                  Text(S.of(context).byEmailText),
                  const Spacer(),
                  const Icon(Icons.arrow_forward_ios),
                ],
              ),
            ),
          ),
        ),
        const SizedBox(height: 24.0),
        InkWell(
          onTap: () => _authBloc.add(ChoiceResetType(type: ResetPasswordType.sms)),
          child: Container(
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
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Row(
                children: [
                  const Icon(Icons.perm_phone_msg),
                  const SizedBox(width: 4.0),
                  Text(S.of(context).bySmsText),
                  const Spacer(),
                  const Icon(Icons.arrow_forward_ios),
                ],
              ),
            ),
          ),
        )
      ],
    );
  }
}

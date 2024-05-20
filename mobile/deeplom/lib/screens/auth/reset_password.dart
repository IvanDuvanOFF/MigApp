import 'package:deeplom/domain/repositories/auth/abstract_auth_repository.dart';
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
                              child: const Padding(
                                padding: EdgeInsets.symmetric(horizontal: 32.0, vertical: 16.0),
                                child: Text('Далее'),
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
            child: const Padding(
              padding: EdgeInsets.all(16.0),
              child: Row(
                children: [
                  Icon(Icons.email_outlined),
                  SizedBox(width: 4.0),
                  Text('По E-mail'),
                  Spacer(),
                  Icon(Icons.arrow_forward_ios),
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
            child: const Padding(
              padding: EdgeInsets.all(16.0),
              child: Row(
                children: [
                  Icon(Icons.perm_phone_msg),
                  SizedBox(width: 4.0),
                  Text('По SMS'),
                  Spacer(),
                  Icon(Icons.arrow_forward_ios),
                ],
              ),
            ),
          ),
        )
      ],
    );
  }
}

import 'dart:async';
import 'dart:io';
import 'package:deeplom/config/di.dart';
import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/config/proxy.dart';
import 'package:deeplom/generated/l10n.dart';
import 'package:deeplom/screens/auth/auth_bloc.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:get_it/get_it.dart';
import 'package:permission_handler/permission_handler.dart';

import 'screens/auth/auth_state.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  diRegister();
  // Load the certificate
  final certData = await rootBundle.load('assets/charles.pem');
  final certBytes = certData.buffer.asUint8List();

  // Set up the custom HttpOverrides
  HttpOverrides.global = MyHttpOverrides(certBytes);
  ByteData data = await PlatformAssetBundle().load('assets/charles.pem');
  SecurityContext.defaultContext.setTrustedCertificatesBytes(data.buffer.asUint8List());

  CustomProxy proxy = await loadProxySettings();
  proxy.enable();

  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    _requestPermission();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (_) => GetIt.I<AuthBloc>(),
      child: Builder(
        builder: (context) {
          return MaterialApp.router(
            debugShowCheckedModeBanner: false,
            localizationsDelegates: const [
              S.delegate,
              GlobalMaterialLocalizations.delegate,
              GlobalWidgetsLocalizations.delegate,
              GlobalCupertinoLocalizations.delegate,
            ],
            supportedLocales: S.delegate.supportedLocales,
            theme: ThemeData(
              useMaterial3: false,
            ),
            routerConfig: appRouter,
          );
        },
      ),
    );
  }
}

Future<void> _requestPermission() async {
  var status = await Permission.storage.request();
  if (status.isGranted) {
    print('Storage permission granted');
  } else {
    print('Storage permission denied');
  }
}

import 'dart:developer';

import 'package:deeplom/config/navigation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  final _secureStorage = const FlutterSecureStorage();

  @override
  void initState() {
    super.initState();
    _checkToken();
  }

  Future<void> _checkToken() async {
    String? token = await _secureStorage.read(key: 'token');
    log('SPLASH TOKEN:: $token');
    if (token != null) {
      // Токен найден, переходим на ProfileScreen
      AppRouting.toMenu();
    } else {
      // Токен не найден, переходим на AuthScreen
      AppRouting.toAuth();
    }
  }

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: CircularProgressIndicator(),
      ),
    );
  }
}

import 'package:deeplom/config/di.dart';
import 'package:deeplom/config/navigation.dart';
import 'package:flutter/material.dart';

void main() {
  diRegister();

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: false,
      ),
      routerConfig: appRouter,
    );
  }
}

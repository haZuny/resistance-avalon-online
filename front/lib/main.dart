import 'package:flutter/material.dart';
import 'package:front/pages/game_page.dart';
import 'package:front/pages/login_page.dart';
import 'package:front/utils/theme_style.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Resistance Avalon',
      theme: ThemeStyle.themeData,
      home: LoginPage(),
    );
  }
}

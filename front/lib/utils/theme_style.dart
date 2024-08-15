import 'package:flutter/material.dart';

class ThemeStyle {
  static ThemeData themeData = ThemeData(
    textTheme: const TextTheme(
      headlineMedium: TextStyle(
        fontSize: 40,
        fontWeight: FontWeight.bold
      ),
      headlineSmall: TextStyle(
        fontSize: 35,
      ),
    ),
  );
}
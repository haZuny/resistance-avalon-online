import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:front/widgets/login_button.dart';

class LoginPage extends StatefulWidget {

  LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _nicknameController = TextEditingController();

  final TextEditingController _roomCodeController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    var textTheme = Theme.of(context).textTheme;

    return Scaffold(
      body: Center(
        child: Column(
          children: [
            const SizedBox(height: 40,),
            Text("레지스탕스 아발론 온라인",
              style: textTheme.headlineMedium,),
            const SizedBox(height: 40,),
            ConstrainedBox(
              constraints: const BoxConstraints(
                  maxWidth: 500
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  TextField(
                    controller: _nicknameController,
                    decoration: const InputDecoration(
                      labelText: "닉네임",
                    ),
                    inputFormatters: [
                      LengthLimitingTextInputFormatter(8)
                    ],
                  ),
                  const SizedBox(height: 20,),
                  TextField(
                    controller: _roomCodeController,
                    decoration: const InputDecoration(
                      labelText: "방 코드",
                      hintText: '6자리 코드를 입력해 주세요',
                    ),
                    inputFormatters: [
                      LengthLimitingTextInputFormatter(6),
                      FilteringTextInputFormatter.digitsOnly,
                    ],
                  ),
                  const SizedBox(height: 40,),
                  LoginButton(
                    nicknameController: _nicknameController,
                    roomCodeController: _roomCodeController,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

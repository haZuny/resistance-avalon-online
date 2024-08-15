import 'package:flutter/material.dart';
import 'package:front/pages/room_page.dart';

class LoginButton extends StatefulWidget {
  final TextEditingController roomCodeController;
  final TextEditingController nicknameController;
  const LoginButton({super.key, required this.roomCodeController, required this.nicknameController});

  @override
  State<LoginButton> createState() => _LoginButtonState();
}

class _LoginButtonState extends State<LoginButton> {
  bool _isJoinable = false;

  @override
  void initState() {
    super.initState();
    widget.roomCodeController.addListener(updateState);
  }

  @override
  void dispose() {
    super.dispose();
    widget.roomCodeController.removeListener(updateState);
  }

  void updateState() {
    setState(() {
      _isJoinable = widget.roomCodeController.text.length == 6;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        ElevatedButton(
          onPressed: () {
            Navigator.push(
              context,
              MaterialPageRoute(builder: (builder)=>RoomPage(isHost: true))
            );
          },
          child: const Text("방 생성",),
        ),
        ElevatedButton(
          onPressed: _isJoinable? (){
            Navigator.push(
                context,
                MaterialPageRoute(builder: (builder)=>RoomPage(isHost: false))
            );
          } : null,
          child: const Text("방 참가",),
        )
      ],
    );
  }
}

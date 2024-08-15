import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:front/pages/game_page.dart';
import 'package:front/widgets/room_user_list.dart';

class RoomPage extends StatelessWidget {
  final bool isHost;


  RoomPage({super.key, required this.isHost});

  @override
  Widget build(BuildContext context) {
    var textTheme = Theme.of(context).textTheme;

    return Scaffold(
      body: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 400),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 20,),
              Text("방 코드 : 111111",
                style: textTheme.headlineMedium,
              ),
              const SizedBox(height: 10,),
              if(isHost) ElevatedButton(onPressed: (){
                Navigator.push(
                    context,
                    MaterialPageRoute(builder: (builder)=>GamePage(11))
                );
              }, child: const Text("게임 시작")),
              const SizedBox(height: 20,),

              //Expanded(child: RoomUserList())
            ],
          ),
        ),
      ),
    );
  }
}

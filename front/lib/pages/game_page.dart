import 'package:flutter/material.dart';
import 'package:front/models/user.dart';
import 'package:front/widgets/game/game_board.dart';
import 'package:front/widgets/game/game_user_info.dart';
import 'package:front/widgets/game/game_vote_count.dart';

class GamePage extends StatelessWidget {
  //test
  final int roomId;
  final List<User> users = [
    User(nickName: "test", keyValue: "dd"),
    User(nickName: "Test2", keyValue: "k11"),
    User(nickName: "Test3", keyValue: "k11"),
    User(nickName: "폭풍저그홍진호", keyValue: "k11"),
    User(nickName: "Test5", keyValue: "k11"),
    User(nickName: "Test6", keyValue: "k11"),
    User(nickName: "붉은바람대나무숲", keyValue: "k11"),
  ];

  GamePage(this.roomId, {super.key});

  @override
  Widget build(BuildContext context) {
    var textTheme = Theme.of(context).textTheme;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.red,
      ),
      body: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 450),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 40,),
              const GameBoard(),
              const SizedBox(height: 20),
              const GameVoteCount(vote: 2,),
              const SizedBox(height: 20,),
              const Text("원정 인원 0/2",
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 22,
                ),
              ),
              Wrap(
                spacing: 5,
                runSpacing: 5,
                children: List.generate(
                  users.length,
                      (idx) {
                    return Card(
                      elevation: 5,
                      child: InkWell(
                        borderRadius: BorderRadius.circular(10),
                        onTap: (){
                        },
                        child: Padding(
                          padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                          child: Text(users[idx].nickName),
                        ),
                      ),
                    );
                  },
                ),
              ),
              const SizedBox(height: 40,),
              GameUserInfo(),
            ],
          ),
        ),
      ),
    );
  }
}

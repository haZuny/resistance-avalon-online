import 'package:flutter/material.dart';

class GameQuest extends StatelessWidget {
  final int idx;
  final int playerNeeded;
  final Color? color;
  const GameQuest({super.key,
    required this.idx,
    required this.playerNeeded,
    this.color,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 100,
      height: 100,
      decoration: BoxDecoration(
        color: color,
        shape: BoxShape.circle,
        border: Border.all(color: Colors.black),
      ),
      child: color==null ? Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text('Quest ${idx + 1}'),
          const SizedBox(height: 8),
          Text("$playerNeeded",
            style: const TextStyle(
              fontSize: 30,
              fontWeight: FontWeight.bold
            ),),
        ],
      ) : null,
    );
  }
}

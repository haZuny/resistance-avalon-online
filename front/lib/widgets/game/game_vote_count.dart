import 'package:flutter/material.dart';

class GameVoteCount extends StatelessWidget {
  final int vote;
  const GameVoteCount({required this.vote, super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 50,
      child: ListView(
        scrollDirection: Axis.horizontal,
        children: List.generate(5, (idx) {
          return Padding(
            padding: const EdgeInsets.symmetric(horizontal: 4),
            child: Container(
              width: 50,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                border: Border.all(color: Colors.black),
                color: vote==idx ? Colors.black : idx==4 ? Colors.red : null
              ),
              child: vote!=idx ? Center(child: Text("$idx",
                style: const TextStyle(
                  fontSize: 25,
                  ),)
              ) : null,
            ),
          );
        }),
      ),
    );
  }
}

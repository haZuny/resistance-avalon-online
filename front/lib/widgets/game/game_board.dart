import 'package:flutter/material.dart';
import 'package:front/widgets/game/game_quest.dart';

class GameBoard extends StatelessWidget {
  const GameBoard({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 200,
      child: Stack(
        children: List.generate(5, (idx) {
          final double offsetX = 80.0 * idx; // 짝수와 홀수 요소의 x 위치 차이
          final double offsetY = 80.0 * (idx % 2); // y 위치 차이

          return Positioned(
              left: offsetX,
              top: offsetY,
              child: GameQuest(
                idx: idx,
                playerNeeded: 1,
              ),
          );
        }),
      ),
    );
  }
}

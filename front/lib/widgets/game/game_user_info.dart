import 'package:flutter/material.dart';

class GameUserInfo extends StatefulWidget {
  const GameUserInfo({super.key});

  @override
  State<GameUserInfo> createState() => _GameUserInfoState();
}

class _GameUserInfoState extends State<GameUserInfo> {
  bool _reveal = true;

  @override
  Widget build(BuildContext context) {
    var textTheme = Theme.of(context).textTheme;

    return SizedBox(
      height: 300,
      child: InkWell(
        borderRadius: BorderRadius.circular(10),
        onTap: (){
          setState(() {
            _reveal = !_reveal;
          });
        },
        child: Stack(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                SizedBox(width: 20,),
                Image.asset('asset/merlin.webp',
                  height: 300,
                ),
                SizedBox(width: 20,),
                Flexible(
                  child: SizedBox(
                    height: 300,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text("멀린",
                          style: textTheme.headlineMedium,),
                        const Text("모드레드를 제외한 모든 악 인원을 볼 수 있습니다."),
                        const Text("현재 악 인원 : TEXT1")
                      ],
                    ),
                  ),
                )
              ],
            ),
            if(_reveal) Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(10),
                color: Colors.grey[800],
              ),
              height: 300,
              alignment: Alignment.center,
              child: const Text(
                '?',
                style: TextStyle(
                    color: Colors.grey,
                    fontSize: 100
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

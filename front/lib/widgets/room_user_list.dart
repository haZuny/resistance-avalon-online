import 'package:flutter/material.dart';
import 'package:front/data/online_data.dart';

class RoomUserList extends StatefulWidget {
  const RoomUserList({super.key});

  @override
  State<RoomUserList> createState() => _RoomUserListState();
}

class _RoomUserListState extends State<RoomUserList> {
  final OnlineData _sseClient = OnlineData('http://10.0.2.2:5000/sse');
  final List<String> _items = [];

  @override
  void initState() {
    super.initState();
    _sseClient.connect();
    _sseClient.stream.listen((event) {
      setState(() {
        _items.add(event);
      });
    });
  }

  @override
  void dispose() {
    _sseClient.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: _items.length,
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(_items[index]),
        );
      },
    );
  }
}

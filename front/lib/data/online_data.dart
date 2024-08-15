import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;

class OnlineData {
  final String url;
  late http.Client _client;
  late StreamController<String> _streamController;

  OnlineData(this.url) {
    _client = http.Client();
    _streamController = StreamController<String>();
  }

  Stream<String> get stream => _streamController.stream;

  void connect() {
    _client.send(http.Request('GET', Uri.parse(url)))
        .then((response) {
      response.stream.transform(utf8.decoder)
          .transform(const LineSplitter())
          .where((event) => event.isNotEmpty)
          .where((event) => event.startsWith('data:'))
          .map((event) => event.substring(5).trim())
          .listen((event) {
        _streamController.add(event);
      }, onError: (error) {
        print('Error: $error');
        _streamController.addError(error);
      });
    });
  }

  void close() {
    _client.close();
    _streamController.close();
  }
}
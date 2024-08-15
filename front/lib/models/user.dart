enum RoleKey{
  good, evil, merlin, percival, murderer, morgan, mordred, oberon
}

class User {
  String nickName;
  String keyValue;
  RoleKey key = RoleKey.good;

  User({required this.nickName, required this.keyValue});

  Map<String, dynamic> toMap(){
    return {
      'nickName' : nickName,
      'keyValue' : keyValue,
    };
  }

  factory User.fromMap(Map<String, dynamic> data){
    return User(
      nickName: data['nickName'],
      keyValue: data['keyValue'],
    );
  }
}
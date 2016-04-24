
namespace java edu.ucsd.cse124
namespace py cse124

typedef string Hash
typedef binary Block

struct ReTy {
	1:string status,
	2:Block content
}

struct hashaddr {
	1:Hash hashValue,
	2:string IPAddress1,
	3:i32 port1,
	4:string IPAddress2,
	5:i32 port2
}

// The block is larger than 16KB.
exception LargeBlockException {
}

// The hash value doesn't match the hash of the actual block itself.
exception InvalidHashException {	
}

// The block doesn't exist on the server.
exception NoSuchBlockException {
}

service TritonTransfer {
	void ping(),

	list<hashaddr> uploadFile(1:string filename, 2:list<Hash> hashlist),

	string uploadBlock(1:Hash hashValue, 2:Block blockValue)
		throws (1:LargeBlockException lblockx, 2:InvalidHashException hashx),

	list<hashaddr> downloadFile(1:string filename),

	ReTy downloadBlock(1:Hash hashValue)
		throws (1:NoSuchBlockException nblockx),

	void sendAddr(1:string IPAddress, 2:i32 port)
}
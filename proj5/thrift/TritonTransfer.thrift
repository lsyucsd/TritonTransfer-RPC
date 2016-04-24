
namespace java edu.ucsd.cse124
namespace py cse124

typedef string Hash
typedef binary Block

struct ReTy {
	1:string status,
	2:Block content
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

	list<Hash> uploadFile(1:string filename, 2:list<Hash> hashlist),

	string uploadBlock(1:Hash hashValue, 2:Block blockValue)
		throws (1:LargeBlockException lblockx, 2:InvalidHashException hashx),

	list<Hash> downloadFile(1:string filename),

	ReTy downloadBlock(1:Hash hashValue)
		throws (1:NoSuchBlockException nblockx)
}
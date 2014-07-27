var http = require("http"),
    url = require("url"),
    path = require("path"),
    fs = require("fs"),
    port = process.argv[2] || 8888;

var mimeTypes = {
    "html": "text/html",
    "jpeg": "image/jpeg",
    "jpg": "image/jpeg",
    "png": "image/png",
    "js": "text/javascript",
    "css": "text/css" };
	
var server = http.createServer(function(request, response) {

  var uri = url.parse(request.url).pathname;
  var filePath = path.join(process.cwd(), uri);
  
  fs.exists(filePath, function(exists) {
    if (!exists) {
      response.writeHead(404, {"Content-Type": "text/plain"});
      response.write("404 Not Found\n");
      response.end();
	  
      return;
    }

    if (fs.statSync(filePath).isDirectory()) {
		filePath += '/index.html';
	}

    fs.readFile(filePath, "binary", function(errorInfo, file) {
      if (errorInfo) {        
        response.writeHead(500, {"Content-Type": "text/plain"});
        response.write(errorInfo + "\n");
        response.end();
		
        return;
      }
	  
	  var fileExtension = path.extname(filePath).split(".")[1];
	  var mimeType = mimeTypes[fileExtension];

      response.writeHead(200, {"Content-Type": mimeType});
      response.write(file, "binary");
      response.end();
    });
  });
});

server.listen(parseInt(port, 10));

console.log("Static file server running at http://localhost:" + port + "/\nCTRL + C to shutdown");

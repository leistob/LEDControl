import SocketServer
from subprocess import call

class MyTCPHandler(SocketServer.BaseRequestHandler):
   """
   The RequestHandler class for our server.

   It is instantiated once per connection to the server, and must
   override the handle() method to implement communication to the
   client.
   """

   def handle(self):
       # self.request is the TCP socket connected to the client
       self.data = self.request.recv(1024).strip()
       #print "{} wrote:".format(self.client_address[0])
       print self.data

       #call(["sudo", "433Utils/RPi_utils/./codesend", self.data])
       
       # just send back the same data, but upper-cased
       self.request.sendall(self.data.upper())

if __name__ == "__main__":
   HOST, PORT = "127.0.0.1", 80

   # Create the server, binding to localhost on port 8080
   server = SocketServer.TCPServer((HOST, PORT), MyTCPHandler)

   # Activate the server; this will keep running until you
   # interrupt the program with Ctrl-C
   server.serve_forever()

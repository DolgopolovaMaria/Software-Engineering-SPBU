using System;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;


namespace EchoServer
{
    class Program
    {
        static async Task Main(string[] args)
        {
            Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            IPEndPoint ipEndPoint = null;
            bool correctStart = false;
            string hostName = Dns.GetHostName();
            IPHostEntry hostEntry = Dns.GetHostEntry(hostName);
            Console.WriteLine("Host name: " + hostName);

            while (!correctStart)
            {
                Console.WriteLine("Input your port ");
                int port = int.Parse(Console.ReadLine());
                try
                {
                    for (int i = 0; i < hostEntry.AddressList.Length; i++)
                    {
                        var address = hostEntry.AddressList[i];
                        Console.WriteLine($"{i}: {address}");
                    }

                    Console.Write($"Please, enter index of the IP address to use (0..{hostEntry.AddressList.Length - 1}):");
                    int selectedIndex = int.Parse(Console.ReadLine());
                    ipEndPoint = new IPEndPoint(hostEntry.AddressList[selectedIndex], port);
                    Console.WriteLine("Host addresses: " + hostEntry.AddressList[selectedIndex]);
                    socket.Bind(ipEndPoint);
                    socket.Listen(50);
                    Console.WriteLine("Started echo server");
                }
                catch
                {
                    Console.WriteLine("This port is already in use");
                    continue;
                }
                correctStart = true;
            }
            while (true)
            {
                Socket client = await socket.AcceptAsync();
                Communication(client);
            }
        }
        
        private static async void Communication(Socket client)
        {
            try
            {
                Console.WriteLine("Got a client");
                var buffer = new byte[1000];
                ArraySegment<byte> segment = new ArraySegment<byte>(buffer);
                int numberOfReceivedBytes;
                while ((numberOfReceivedBytes = await client.ReceiveAsync(segment, SocketFlags.None)) > 0)
                {
                    Console.WriteLine("Read: " + Encoding.ASCII.GetString(segment.Array, 0, segment.Count));
                    Console.WriteLine("Sending bytes back");
                    var send = segment.ToArray().Take(numberOfReceivedBytes).ToArray();
                    await client.SendAsync(new ArraySegment<byte>(send), SocketFlags.None);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}
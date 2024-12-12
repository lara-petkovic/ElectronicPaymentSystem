import React, { useState, useEffect } from "react";
import "./App.css";
import Web3 from "web3";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

function App() {
  const [amount, setAmount] = useState(null);
  const [merchantId, setMerchantId] = useState(null);
  const [transactionId, setTransactionId] = useState(null);
  const [destinationAddress, setDestinationAddress] = useState("");
  const [eth, setTEuri] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const web3 = new Web3(window.ethereum);

  // WebSocket connection setup
  useEffect(() => {
    const socket = new SockJS("http://localhost:8088/ws");
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
    });

    client.onConnect = () => {
      console.log("Connected to WebSocket");
      client.subscribe("/topic/string-data", (msg) => {
        const [receivedAmount, receivedMerchantId, receivedTransactionId,eth] = msg.body.split(",");
        setAmount(receivedAmount);
        setMerchantId(receivedMerchantId);
        setTransactionId(receivedTransactionId);
        setTEuri(eth);
        setShowForm(true);
        console.log("Transaction details received");
      });
    };

    client.onStompError = (frame) => {
      console.error("WebSocket error: ", frame.headers["message"]);
    };

    client.activate();

    return () => client.deactivate();
  }, []);

  async function enableMetaMask() {
    if (typeof window.ethereum !== "undefined") {
      console.log("MetaMask is installed!");
    } else {
      console.log("MetaMask is not installed!");
    }
    try {
      await window.ethereum.request({ method: "eth_requestAccounts" });
    } catch (error) {
      console.error("User denied account access");
    }
  }

  async function sendEther(to, amount) {
    const from = "0xe338ef3F5b907E62b40655570D0A3eB642Bd8d13";
   
    const formattedAmount = parseFloat(amount).toFixed(18); 
    const value = web3.utils.toWei(formattedAmount, "ether");

    try {
      const response = await web3.eth.sendTransaction({
        from,
        to,
        value,
      });
      console.log(response);
      toast.success("Transaction successful: " + response.transactionHash, {
        autoClose: 3000,
      });
      setShowForm(false); // Hide the form after successful transaction
    } catch (error) {
      toast.error(error.message, { autoClose: 3000 });
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    enableMetaMask();
    sendEther(destinationAddress, eth);
  };

  return (
    <div className="main-div">
      <div className="center">
        {showForm ? (
          <form onSubmit={handleSubmit}>
            <label>{amount} EUR = {eth} SepoliaETH</label>
            <br></br>
            <label htmlFor="destinationAddress">Destination Address:</label>
            <input
              type="text"
              id="destinationAddress"
              value={destinationAddress}
              onChange={(e) => setDestinationAddress(e.target.value)}
              required
            />
            <button type="submit">Submit</button>
          </form>
        ) : (
          <h2>Waiting for transaction...</h2>
        )}
        <ToastContainer />
      </div>
    </div>
  );
}

export default App;

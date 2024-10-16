// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract TokenDistributor is Ownable {
    IERC20 public token;

    constructor(address _tokenAddress) {
        token = IERC20(_tokenAddress);
    }

    modifier onlyOwnerModifier() {
        require(msg.sender == owner(), "Caller is not the owner");
        _;
    }

    function claim(address recipient, uint256 amount) external onlyOwnerModifier {
        require(token.balanceOf(address(this)) >= amount, "Not enough tokens in the contract");
        token.transfer(recipient, amount);
    }

    function withdraw(uint256 amount) external onlyOwnerModifier {
        require(token.balanceOf(address(this)) >= amount, "Not enough tokens in the contract");
        token.transfer(owner(), amount);
    }
}

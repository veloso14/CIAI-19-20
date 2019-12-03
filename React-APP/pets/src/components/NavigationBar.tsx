import React from 'react';
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";

const NavigationBar = () => {
    return (
        <Navbar bg="dark" variant="dark">
            <Navbar.Brand href="/">Vetclinic</Navbar.Brand>
            <Nav className="mr-auto">
                <Nav.Link href="/" >Home</Nav.Link>
                <Nav.Link href="/pet">Pet</Nav.Link>
                <Nav.Link href="/appointment">Create Appointment</Nav.Link>
                <Nav.Link href="/admin">Admin</Nav.Link>
            </Nav>
        </Navbar>
    );
}

export default NavigationBar;
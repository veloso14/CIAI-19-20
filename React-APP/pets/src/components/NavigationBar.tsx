import React from 'react';
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import { useHistory } from 'react-router-dom';

const NavigationBar = (props: { performSignOut: () => void, currentRole: string }) => {

    return (
        <Navbar bg="dark" variant="dark">
            <Navbar.Brand href="/">Vetclinic</Navbar.Brand>
            <Nav className="mr-auto">
                <Nav.Link href="/">Home</Nav.Link>
                {props.currentRole == "CLIENT" && <Nav.Link href="/client">Client</Nav.Link>}
                {props.currentRole == "ROLE_ADMIN" && <Nav.Link href="/admin">Admin</Nav.Link>}
                {props.currentRole == "ROLE_VET" &&  <Nav.Link href="/vet">Vets</Nav.Link>}
            </Nav>
            <Navbar.Collapse>
                <Nav className="justify-content-end" style={{width: "100%"}}>
                    {props.currentRole == "ROLE_ADMIN" && <Nav.Link href="/profile">Profile</Nav.Link>}
                    <Nav.Link onClick={props.performSignOut}>Logout</Nav.Link>
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );
}

export default NavigationBar;
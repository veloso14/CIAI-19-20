import React, {useEffect, useState} from 'react';
import {getData} from "../Utils/NetworkUtils";
import PetList from "../components/PetList";
import {Appointment} from "../components/AppointmentList"
import Container from "react-bootstrap/Container";
import Spinner from "react-bootstrap/Spinner";
import AddAppointmentForm from "../components/AddAppointmentForm";
import AppointmentList from "../components/AppointmentList";
import Accordion from "react-bootstrap/Accordion";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";


export interface Client {
    id: number,
    name: string,
    username: string
}

const ClientPage = () => {
    const [client, setClient] = useState({} as Client);
    const [appointments, setAppointments] = useState([] as Appointment[]);
    const [loading, setLoading] = useState(false);

    const loadClient = (id: string) => {
        setLoading(true);
        return getData(`/clients/${id}`, {} as Client)
            .then(data => {
                console.log("client loaded: " + JSON.stringify(data));
                setClient(data);
                setLoading(false);
            })
    };

    const loadAppointments = (id: string) => {
        setLoading(true);
        return getData(`/clients/${id}/appointments`, [] as Appointment[])
            .then(data => {
                console.log("client loaded: " + JSON.stringify(data));
                setAppointments(data);
                setLoading(false);
            })
    };

    const clientId = "571";

    useEffect(() => {
        loadClient(clientId)
        loadAppointments(clientId)
    }, []);

    const content = (
        <div>
            <h1 className="text-center">Welcome {client.name}!</h1>
            <PetList/> <br/>
            <AppointmentList appointments={appointments}/>
            <Accordion>
                <Card>
                    <Card.Header>
                        <Accordion.Toggle as={Button} variant="link" eventKey="0">
                            Add new appointment
                        </Accordion.Toggle>
                    </Card.Header>
                    <Accordion.Collapse eventKey="0">
                        <Card.Body>
                            <AddAppointmentForm/>
                        </Card.Body>
                    </Accordion.Collapse>
                </Card>
                <br/>


            </Accordion>
        </div>
    );

    const loadingContent = (
        <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
        </Spinner>
    );

    return (
        <Container>
            {loading ? loadingContent : content}
        </Container>
    );
};

export default ClientPage;
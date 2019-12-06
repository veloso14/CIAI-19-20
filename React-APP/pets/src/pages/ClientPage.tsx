import React, {useEffect, useState} from 'react';
import {getData} from "../Utils/NetworkUtils";
import PetList, {Pet} from "../components/PetList";
import AppointmentList, {Appointment} from "../components/AppointmentList"
import Container from "react-bootstrap/Container";
import Spinner from "react-bootstrap/Spinner";
import AddAppointmentForm from "../components/AddAppointmentForm";
import Accordion from "react-bootstrap/Accordion";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import {GlobalState} from "../App";
import {connect} from "react-redux";


export interface Client {
    id: number,
    name: string,
    username: string
}

const ClientPage = (props: {
    currentUser: string,
    currentRole: string,
}) => {
    const [client, setClient] = useState({} as Client);
    const [appointments, setAppointments] = useState([] as Appointment[]);
    const [pets, setPets] = useState([] as Pet[]);
    const [loading, setLoading] = useState(false);


    const loadClient = (username: string) => {
        setLoading(true);
        return getData(`/clients/client/${username}`, {} as Client)
            .then(data => {
                console.log("client loaded: " + JSON.stringify(data));
                setClient(data);
                setLoading(false);
            })
    };

    const loadAppointments = () => {
        setLoading(true);
        return getData(`/clients/${client.id}/appointments`, [] as Appointment[])
            .then(data => {
                console.log("apt loaded: " + JSON.stringify(data));
                setAppointments(data);
                setLoading(false);
            })
    };

    const loadPets = () => {
        setLoading(true);
        return getData(`/clients/${client.id}/pets`, [] as Pet[])
            .then(data => {
                console.log("pets loaded: " + JSON.stringify(data));
                setPets(data);
                setLoading(false);
            })
    };

    useEffect(() => {
        loadClient(props.currentUser)
        console.log(props.currentRole)
    }, []);

    useEffect(() => {
        if (typeof client.id != "undefined") {
            loadAppointments()
            loadPets()
        }
    }, [client]);

    const content = (
        <div>
            <h1 className="text-center">Welcome {props.currentUser}!</h1>
            <PetList currentUserId={client.id} pets={pets}/> <br/>
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
                            <AddAppointmentForm currentUserId={client.id}/>
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

const mapStateToProps = (state: GlobalState) => ({
    currentUser: state.signIn.currentUser,
    currentRole: state.signIn.currentRole
});

const mapDispatchToProps = (dispatch: any) => {
    return {}
};
export default connect(mapStateToProps, mapDispatchToProps)(ClientPage);


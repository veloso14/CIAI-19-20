import React, {useEffect, useState} from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {GlobalState} from "../App";
// @ts-ignore
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import {Link} from "react-router-dom"
import Container from "react-bootstrap/Container";
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";
import {fetchVetAppointments} from "../actions/ScheduleAction";
import '../main.css'
import {Appointment} from "./AppointmentList";
import {getData} from "../Utils/NetworkUtils";
import {Client} from "../pages/ClientPage";
import {Pet} from "./PetList";

export interface AppointmentState {
    apt:Appointment,
    appointments: Appointment[],
    isFetching: boolean
}



export interface Event {
    title: string,
    date: string
}

const ProtoAppointmentList = (props: {currentUser: string,
    currentRole: string,
    state: GlobalState, appointments: Appointment[], isFetching: boolean, loadAppointment: (id: number)
        => void, postAppointment: (appointment: Appointment) => void, deleteAppointment: (id: number) => void
}) => {
    const [client, setClient] = useState({} as Client);
    const [appointments, setAppointments] = useState([] as Appointment[]);
    const [loading, setLoading] = useState(false);

    const loadClient = (username: string) => {
        setLoading(true);
        return getData(`/vets/vet/${username}`, {} as Client)
            .then(data => {
                console.log("client loaded: " + JSON.stringify(data));
                setClient(data);
                setLoading(false);
            })
    };


    const loadAppointments = () => {
        setLoading(true);
        return getData(`/vets/${client.id}/appointments`, [] as Appointment[])
            .then(data => {
                console.log("apt loaded: " + JSON.stringify(data));
                setAppointments(data);
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
        }
    }, [client]);

    let events : Event[] = [];

    let list = appointments.map((appointment: Appointment) => {
        events.push({title: "Appointment",  date: appointment.date.toString().substr(0,10)})
        return (
            <ListGroup.Item key={appointment.id}>
                <Link to={`/appointment/${appointment.id}/`}>{appointment.date.toString().substr(0,10)}</Link>
            </ListGroup.Item>
        )
    });

    let emptyList = (<p className="text-center">You currently don't have any appointments!</p>)

    let aptList = ((appointments.length > 0) ? <ListGroup>{list}</ListGroup> : emptyList)


    return (
        <Container>
            <br/>
            <h1 className="text-center">My Appointments</h1>
            <br/>

            {props.isFetching ? <p>Loading...</p> : aptList}

            <br/>
            <h1 className="text-center">Appointments</h1>
            <div>


                <FullCalendar defaultView="dayGridMonth"
                              plugins={[dayGridPlugin]}
                              events={events}/>
            </div>

        </Container>
    );
};


const mapStateToProps = (state: GlobalState) => ({
    currentUser: state.signIn.currentUser,
    currentRole: state.signIn.currentRole,
    appointments: state.appointments.appointments,
    isFetching: state.appointments.isFetching
});


const mapDispatchToProps = (dispatch: any) => {
    return {
        loadAppointment: (id: number) => {
            dispatch(fetchVetAppointments(id))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoAppointmentList);